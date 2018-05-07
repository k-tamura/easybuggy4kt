package org.t246osslab.easybuggy4kt.vulnerabilities

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.DefaultLoginController
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class BruteForceController : DefaultLoginController() {

    @RequestMapping(value = "/bruteforce/login", method = arrayOf(RequestMethod.GET))
    override fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        req.setAttribute("note", msg?.getMessage("msg.note.brute.force", null, locale))
        super.doGet(mav, req, res, locale)
        return mav
    }

    @RequestMapping(value = "/bruteforce/login", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    override fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {

        val userid = req.getParameter("userid")
        val password = req.getParameter("password")

        val session = req.getSession(true)
        if (authUser(userid, password)) {
            session.setAttribute("authNMsg", "authenticated")
            session.setAttribute("userid", userid)

            val target = session.getAttribute("target") as String?
            if (target == null) {
                res.sendRedirect("/admins/main")
            } else {
                session.removeAttribute("target")
                res.sendRedirect(target)
            }
            return null
        } else {
            session.setAttribute("authNMsg", msg?.getMessage("msg.authentication.fail", null, locale))
        }
        return doGet(mav, req, res, locale)
    }
}
