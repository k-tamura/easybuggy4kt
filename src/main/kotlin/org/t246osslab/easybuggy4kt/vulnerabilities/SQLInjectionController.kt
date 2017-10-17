package org.t246osslab.easybuggy4kt.vulnerabilities

import java.sql.ResultSet
import java.sql.SQLException
import java.util.Locale

import javax.servlet.http.HttpServletRequest

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import org.t246osslab.easybuggy4kt.core.model.User

@Controller
class SQLInjectionController : AbstractController() {

    @Autowired
    internal var jdbcTemplate: JdbcTemplate? = null

    @RequestMapping(value = "/sqlijc")
    fun process(@RequestParam(value = "name", required = false) name: String,
                @RequestParam(value = "password", required = false) password: String, mav: ModelAndView,
                req: HttpServletRequest, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "sqlijc")
        val trimedName = StringUtils.trim(name)
        val trimedPassword = StringUtils.trim(password)
        if (!StringUtils.isBlank(trimedName) && !StringUtils.isBlank(trimedPassword) && trimedPassword.length >= 8) {
            try {
                val users = selectUsers(trimedName, trimedPassword)
                if (users == null || users.isEmpty()) {
                    mav.addObject("errmsg", msg?.getMessage("msg.error.user.not.exist", null, locale))
                } else {
                    mav.addObject("userList", users)
                }
            } catch (se: DataAccessException) {
                log.error("DataAccessException occurs: ", se)
                mav.addObject("errmsg", msg?.getMessage("msg.db.access.error.occur", null, locale))
            }

        } else {
            if (req.method.equals("POST", ignoreCase = true)) {
                mav.addObject("errmsg", msg?.getMessage("msg.warn.enter.name.and.passwd", null, locale))
            }
        }
        return mav
    }

    private fun selectUsers(name: String, password: String): List<User>? {

        return jdbcTemplate!!.query("SELECT name, secret FROM users WHERE ispublic = 'true' AND name='" + name
                + "' AND password='" + password + "'", RowMapper<Any> { rs, rowNum ->
            val user = User()
            user.setName(rs.getString("name"))
            user.setSecret(rs.getString("secret"))
            user
        })
    }
}
