package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang.StringUtils
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
class LDAPInjectionController : DefaultLoginController() {

    @RequestMapping(value = "/ldapijc/login", method = arrayOf(RequestMethod.GET))
    override fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        req.setAttribute("note", msg?.getMessage("msg.note.ldap.injection", null, locale))
        super.doGet(mav, req, res, locale)
        return mav
    }

    @RequestMapping(value = "/ldapijc/login", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    override fun doPost(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {
        return super.doPost(mav, req, res, locale)
    }

    override fun authUser(userId: String?, password: String?): Boolean {

        if (StringUtils.isBlank(userId) || userId!!.length < 5 || StringUtils.isBlank(password)
                || password!!.length < 8) {
            return false
        }
        try {
            val query = LdapQueryBuilder.query()
                    .filter("(&(uid=" + userId?.trim { it <= ' ' } + ")(userPassword=" + password?.trim { it <= ' ' } + "))")
            val users = ldapTemplate?.find(query, User::class.java)
            if (users!!.isEmpty()) {
                return false
            }
        } catch (e: EmptyResultDataAccessException) {
            return false
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            return false
        }

        return true
    }
}
