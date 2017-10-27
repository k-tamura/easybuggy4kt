package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ldap.core.LdapTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.io.IOException
import java.util.*
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.naming.directory.BasicAttribute
import javax.naming.directory.DirContext
import javax.naming.directory.ModificationItem
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ClickJackingController : AbstractController() {

    @Autowired
    private var ldapTemplate: LdapTemplate? = null

    @RequestMapping(value = "/admins/clickjacking", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "clickjacking")
        return mav
    }

    @RequestMapping(value = "/admins/clickjacking", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    private fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {
        setViewAndCommonObjects(mav, locale, "clickjacking")

        val session = req.session
        if (session == null) {
            res.sendRedirect("/")
            return null
        }
        val userid = session.getAttribute("userid") as String?
        if (userid == null) {
            res.sendRedirect("/")
            return null
        }
        val mail = StringUtils.trim(req.getParameter("mail"))
        if (!StringUtils.isBlank(mail) && isValidEmailAddress(mail)) {
            try {
                val item = ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        BasicAttribute("mail", mail))
                ldapTemplate!!.modifyAttributes(
                        "uid=" + encodeForLDAP(userid.trim { it <= ' ' }) + ",ou=people,dc=t246osslab,dc=org",
                        arrayOf(item))
                mav.addObject("mail", mail)

            } catch (e: Exception) {
                log.error("Exception occurs: ", e)
                mav.addObject("errmsg", msg?.getMessage("msg.mail.change.failed", null, locale))
                return doGet(mav, req, res, locale)
            }

        } else {
            mav.addObject("errmsg", msg?.getMessage("msg.mail.format.is.invalid", null, locale))
            return doGet(mav, req, res, locale)
        }
        return mav
    }

    fun isValidEmailAddress(email: String): Boolean {
        var result = true
        try {
            val emailAddr = InternetAddress(email)
            emailAddr.validate()
        } catch (e: AddressException) {
            log.debug("Mail address is invalid: " + email, e)
            result = false
        }

        return result
    }
}
