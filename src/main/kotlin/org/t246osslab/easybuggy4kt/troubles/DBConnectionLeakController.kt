package org.t246osslab.easybuggy4kt.troubles

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.ArrayList
import java.util.Locale

import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import org.t246osslab.easybuggy4kt.core.model.User

@Controller
class DBConnectionLeakController : AbstractController() {

    @Value("\${spring.datasource.url}")
    internal var datasourceUrl: String? = null

    @Autowired
    internal var jdbcTemplate: JdbcTemplate? = null

    @RequestMapping(value = "/dbconnectionleak")
    fun process(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "dbconnectionleak")
        if (StringUtils.isBlank(datasourceUrl) || datasourceUrl!!.startsWith("jdbc:derby:memory:")) {
            /* Just show users the warning because this feature can work if an external DB is used. */
            mav.addObject("note", msg?.getMessage("msg.note.not.use.ext.db", null, locale))
            return mav
        } else {
            mav.addObject("note", msg?.getMessage("msg.note.db.connection.leak.occur", null, locale))
        }

        try {
            val users = selectUsers()
            if (users.isEmpty()) {
                mav.addObject("errmsg", msg?.getMessage("msg.error.user.not.exist", null, locale))
            } else {
                mav.addObject("userList", users)
            }
        } catch (se: SQLException) {
            log.error("SQLException occurs: ", se)
            mav.addObject("errmsg", msg?.getMessage("msg.db.access.error.occur", null, locale))
        }

        return mav
    }

    @Throws(SQLException::class)
    private fun selectUsers(): List<User> {
        val users = ArrayList<User>()
        var conn: Connection? = null
        var stmt: Statement? = null
        var rs: ResultSet? = null
        conn = jdbcTemplate!!.dataSource.connection
        stmt = conn!!.createStatement()
        rs = stmt!!.executeQuery("select id, name, phone, mail from users where ispublic = 'true'")
        while (rs!!.next()) {
            val user = User()
            user.userId = rs.getString("id")
            user.name = rs.getString("name")
            user.phone = rs.getString("phone")
            user.mail = rs.getString("mail")
            users.add(user)
        }
        return users
    }
}
