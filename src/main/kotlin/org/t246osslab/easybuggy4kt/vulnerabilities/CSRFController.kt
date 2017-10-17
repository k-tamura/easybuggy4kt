package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang.StringUtils
import org.owasp.esapi.ESAPI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ldap.core.LdapTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.io.IOException
import java.util.*
import javax.naming.directory.BasicAttribute
import javax.naming.directory.DirContext
import javax.naming.directory.ModificationItem
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class CSRFController : AbstractController() {

    @Autowired
    internal var ldapTemplate: LdapTemplate? = null

    @RequestMapping(value = "/admins/csrf", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "csrf")
        return mav
    }

    @RequestMapping(value = "/admins/csrf", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    protected fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {
        setViewAndCommonObjects(mav, locale, "csrf")
        val session = req.session
        if (session == null) {
            res.sendRedirect("/")
            return null
        }
        val userid = session.getAttribute("userid") as String
        val password = StringUtils.trim(req.getParameter("password"))
        if (!StringUtils.isBlank(userid) && !StringUtils.isBlank(password) && password.length >= 8) {
            try {
                val item = ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                        BasicAttribute("userPassword", password))
                ldapTemplate!!.modifyAttributes(
                        "uid=" + ESAPI.encoder().encodeForLDAP(userid.trim { it <= ' ' }) + ",ou=people,dc=t246osslab,dc=org",
                        arrayOf(item))
            } catch (e: Exception) {
                log.error("Exception occurs: ", e)
                mav.addObject("errmsg", msg?.getMessage("msg.passwd.change.failed", null, locale))
                return doGet(mav, locale)
            }

        } else {
            if (StringUtils.isBlank(password) || password.length < 8) {
                mav.addObject("errmsg", msg?.getMessage("msg.passwd.is.too.short", null, locale))
            } else {
                mav.addObject("errmsg", msg?.getMessage("msg.unknown.exception.occur",
                        arrayOf("userid: " + userid), null, locale))
            }
            return doGet(mav, locale)
        }
        mav.addObject("complete", "true")
        return mav
    }
}
