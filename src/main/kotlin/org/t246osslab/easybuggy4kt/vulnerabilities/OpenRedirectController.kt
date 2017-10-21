package org.t246osslab.easybuggy4kt.vulnerabilities

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
class OpenRedirectController : DefaultLoginController() {

    @RequestMapping(value = "/openredirect/login", method = arrayOf(RequestMethod.GET))
    override fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        req.setAttribute("note", msg?.getMessage("msg.note.open.redirect", null, locale))
        super.doGet(mav, req, res, locale)
        return mav
    }

    @RequestMapping(value = "/openredirect/login", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    override fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {

        val userid = req.getParameter("userid")
        val password = req.getParameter("password")
        var loginQueryString: String? = req.getParameter("loginquerystring")
        loginQueryString = if (loginQueryString == null) {
            ""
        } else {
            "?" + loginQueryString
        }

        val session = req.getSession(true)
        if (isAccountLocked(userid)) {
            session.setAttribute("authNMsg", "msg.account.locked")
            res.sendRedirect("/openredirect/login" + loginQueryString)
        } else if (authUser(userid, password)) {
            /* if authentication succeeded, then reset account lock */
            var admin = userLoginHistory[userid]
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

            val gotoUrl = req.getParameter("goto")
            if (gotoUrl != null) {
                res.sendRedirect(gotoUrl)
            } else {
                val target = session.getAttribute("target") as String?
                if (target == null) {
                    res.sendRedirect("/admins/main")
                } else {
                    session.removeAttribute("target")
                    res.sendRedirect(target)
                }
            }
        } else {
            /* account lock count +1 */
            if (userid != null) {
                var admin = userLoginHistory[userid]
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
            res.sendRedirect("/openredirect/login" + loginQueryString)
        }
        return null
    }
}
