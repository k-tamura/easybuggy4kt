package org.t246osslab.easybuggy4kt.controller

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.ldap.AuthenticationException
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.query.LdapQueryBuilder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.core.model.User
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class DefaultLoginController : AbstractController() {

    @Value("\${account.lock.time}")
    internal var accountLockTime: Long = 0

    @Value("\${account.lock.count}")
    internal var accountLockCount: Long = 0

    @Autowired
    protected var ldapTemplate: LdapTemplate? = null

    /* User's login history using in-memory account locking */
    protected var userLoginHistory: ConcurrentHashMap<String, User> = ConcurrentHashMap()

    @RequestMapping(value = "/login", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "login")

        val hiddenMap = HashMap<String, Array<String>>()
        val paramNames = req.parameterNames
        while (paramNames.hasMoreElements()) {
            val paramName = paramNames.nextElement() as String
            hiddenMap.put(paramName, req.getParameterValues(paramName))
            mav.addObject("hiddenMap", hiddenMap)
        }

        val session = req.getSession(true)
        if (session.getAttribute("authNMsg") != null && "authenticated" != session.getAttribute("authNMsg")) {
            mav.addObject("errmsg", msg?.getMessage(session.getAttribute("authNMsg") as String, null, locale))
            session.setAttribute("authNMsg", null)
        }
        return mav
    }

    @RequestMapping(value = "/login", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {

        val userid = StringUtils.trim(req.getParameter("userid"))
        val password = StringUtils.trim(req.getParameter("password"))

        val session = req.getSession(true)
        if (isAccountLocked(userid)) {
            session.setAttribute("authNMsg", "msg.account.locked")
            res.sendRedirect("/login")
        } else if (authUser(userid, password)) {
            /* if authentication succeeded, then reset account lock */
            var admin: User? = userLoginHistory[userid!!]
            if (admin == null) {
                val newAdmin = User()
                newAdmin.userId = userid
                admin = userLoginHistory.putIfAbsent(userid, newAdmin)
                if (admin == null) {
                    admin = newAdmin
                }
            }
            admin.loginFailedCount = 0
            admin.lastLoginFailedTime = null

            session.setAttribute("authNMsg", "authenticated")
            session.setAttribute("userid", userid)

            var target = session.getAttribute("target") as String?
            if (target == null) {
                res.sendRedirect("/admins/main")
            } else {
                session.removeAttribute("target")
                res.sendRedirect(target)
            }
        } else {
            /* account lock count +1 */
            if (userid != null) {
                var admin: User? = userLoginHistory[userid]
                if (admin == null) {
                    val newAdmin = User()
                    newAdmin.userId = userid
                    admin = userLoginHistory.putIfAbsent(userid, newAdmin)
                    if (admin == null) {
                        admin = newAdmin
                    }
                }
                admin.loginFailedCount = admin.loginFailedCount + 1
                admin.lastLoginFailedTime = Date()
            }
            session.setAttribute("authNMsg", "msg.authentication.fail")
            return doGet(mav, req, res, locale)
        }
        return null
    }

    protected fun isAccountLocked(userid: String?): Boolean {
        if (userid == null) {
            return false
        }
        val admin = userLoginHistory[userid]
        return admin != null && admin.loginFailedCount == accountLockCount
                && Date().time - admin.lastLoginFailedTime!!.getTime() < accountLockTime
    }

    protected fun authUser(userId: String?, password: String?): Boolean {
        if (userId == null || password == null) {
            return false
        }
        try {
            /* Perform a simple LDAP 'bind' authentication */
            val query = LdapQueryBuilder.query().where("uid").`is`(userId)
            ldapTemplate!!.authenticate(query, password)
        } catch (e: EmptyResultDataAccessException) {
            return false
        } catch (e: AuthenticationException) {
            return false
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            return false
        }

        return true
    }
}
