package org.t246osslab.easybuggy4kt.vulnerabilities

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.ldap.query.LdapQueryBuilder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.DefaultLoginController
import org.t246osslab.easybuggy4kt.core.model.User
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class VerboseErrorMessageController : DefaultLoginController() {

    @RequestMapping(value = "/verbosemsg/login", method = arrayOf(RequestMethod.GET))
    override fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        req.setAttribute("note", msg?.getMessage("msg.note.verbose.errror.message", null, locale))
        super.doGet(mav, req, res, locale)
        return mav
    }

    @RequestMapping(value = "/verbosemsg/login", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    override fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {

        val userid = req.getParameter("userid")
        val password = req.getParameter("password")

        val session = req.getSession(true)
        if (isAccountLocked(userid)) {
            session.setAttribute("authNMsg", "msg.account.locked")
            return doGet(mav, req, res, locale)
        } else if (!isExistUser(userid)) {
            session.setAttribute("authNMsg", "msg.user.not.exist")
            return doGet(mav, req, res, locale)
        } else if (!password.matches("[0-9a-z]{8}".toRegex())) {
            session.setAttribute("authNMsg", "msg.low.alphnum8")
            return doGet(mav, req, res, locale)
        } else if (authUser(userid, password)) {
            /* if authentication succeeded, then reset account lock */
            var admin = userLoginHistory.get(userid)
            if (admin == null) {
                val newAdmin = User()
                newAdmin.userId = userid
                admin = userLoginHistory.putIfAbsent(userid, newAdmin)
                if (admin == null) {
                    admin = newAdmin
                }
            }
            admin!!.loginFailedCount = 0
            admin!!.lastLoginFailedTime = null

            session.setAttribute("authNMsg", "authenticated")
            session.setAttribute("userid", userid)

            val target = session.getAttribute("target") as String
            if (target == null) {
                res.sendRedirect("/admins/main")
            } else {
                session.removeAttribute("target")
                res.sendRedirect(target)
            }
        } else {
            /* account lock count +1 */
            if (userid != null) {
                var admin = userLoginHistory.get(userid)
                if (admin == null) {
                    val newAdmin = User()
                    newAdmin.userId = userid
                    admin = userLoginHistory.putIfAbsent(userid, newAdmin)
                    if (admin == null) {
                        admin = newAdmin
                    }
                }
                admin!!.loginFailedCount = admin!!.loginFailedCount + 1
                admin!!.lastLoginFailedTime = Date()
            }

            session.setAttribute("authNMsg", "msg.password.not.match")
            return doGet(mav, req, res, locale)
        }
        return null
    }

    private fun isExistUser(username: String?): Boolean {
        try {
            val query = LdapQueryBuilder.query().where("uid").`is`(username)
            val user = ldapTemplate?.findOne(query, User::class.java)
            if (user != null) {
                return true
            }
        } catch (e: EmptyResultDataAccessException) {
            // do nothing if user does not exist
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
        }

        return false
    }
}
