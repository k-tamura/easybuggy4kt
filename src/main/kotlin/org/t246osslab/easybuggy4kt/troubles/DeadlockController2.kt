package org.t246osslab.easybuggy4kt.troubles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.dao.DeadlockLoserDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Controller
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import org.t246osslab.easybuggy4kt.core.model.User
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@Controller
class DeadlockController2 : AbstractController() {

    @Autowired
    internal var jdbcTemplate: JdbcTemplate? = null

    @Autowired
    private val txMgr: PlatformTransactionManager? = null

    @RequestMapping(value = "/deadlock2")
    fun process(req: HttpServletRequest, ses: HttpSession, mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "deadlock2")
        // Overwrite title (because title is the same as xee page)
        mav.addObject("title", msg?.getMessage("title.xee.page", null, locale))
        var users: MutableList<User>?
        val order = getOrder(req)
        if ("POST" == req.method) {
            users = ArrayList<User>()
            var j = 0
            while (true) {
                val uid = req.getParameter("uid_" + (j + 1)) ?: break
                val user = User()
                user.userId = uid
                user.name = req.getParameter(uid + "_name")
                user.phone = req.getParameter(uid + "_phone")
                user.mail = req.getParameter(uid + "_mail")
                users.add(user)
                j++
            }
            updateUsers(users, locale, mav)
        } else {
            users = selectUsers(order, locale, mav)
        }
        mav.addObject("userList", users)
        mav.addObject("order", order)
        return mav
    }

    private fun getOrder(req: HttpServletRequest): String {
        var order = req.getParameter("order")
        if ("asc" == order) {
            order = "desc"
        } else {
            order = "asc"
        }
        return order
    }

    private fun selectUsers(order: String, locale: Locale, mav: ModelAndView): MutableList<User>? {
        var users: MutableList<User>? = null
        try {
            users = jdbcTemplate!!.query("select * from users where ispublic = 'true' order by id " + if ("desc" == order) "desc" else "asc", RowMapper<User> { rs, _ ->
                val user = User()
                user.userId = rs.getString("id")
                user.name = rs.getString("name")
                user.phone = rs.getString("phone")
                user.mail = rs.getString("mail")
                user
            })
        } catch (e: DataAccessException) {
            mav.addObject("errmsg",
                    msg?.getMessage("msg.db.access.error.occur", arrayOf<String?>(e.message), null, locale))
            log.error("DataAccessException occurs: ", e)
        } catch (e: Exception) {
            mav.addObject("errmsg",
                    msg?.getMessage("msg.unknown.exception.occur", arrayOf<String?>(e.message), null, locale))
            log.error("Exception occurs: ", e)
        }

        return users
    }

    private fun updateUsers(users: List<User>, locale: Locale, mav: ModelAndView) {
        val dtDef = DefaultTransactionDefinition()

        val trnStatus = txMgr!!.getTransaction(dtDef)
        var executeUpdate = 0
        try {
            for (user in users) {
                executeUpdate = executeUpdate + jdbcTemplate!!.update("Update users set name = ?, phone = ?, mail = ? where id = ?",
                        user.name, user.phone, user.mail, user.userId)
                log.info(user.userId + " is updated.")
                Thread.sleep(500)
            }
            txMgr!!.commit(trnStatus)
            mav.addObject("msg", msg?.getMessage("msg.update.records", arrayOf<Any>(executeUpdate), null, locale))
        } catch (e: DeadlockLoserDataAccessException) {
            txMgr!!.rollback(trnStatus)
            mav.addObject("errmsg", msg?.getMessage("msg.deadlock.occurs", null, locale))
            log.error("DeadlockLoserDataAccessException occurs: ", e)
        } catch (e: DataAccessException) {
            txMgr!!.rollback(trnStatus)
            mav.addObject("errmsg",
                    msg?.getMessage("msg.db.access.error.occur", arrayOf<String?>(e.message), null, locale))
            log.error("DataAccessException occurs: ", e)
        } catch (e: Exception) {
            txMgr!!.rollback(trnStatus)
            mav.addObject("errmsg",
                    msg?.getMessage("msg.unknown.exception.occur", arrayOf<String?>(e.message), null, locale))
            log.error("Exception occurs: ", e)
        }

    }
}
