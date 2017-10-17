package org.t246osslab.easybuggy4kt.troubles

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.lang.management.ManagementFactory
import java.util.*
import javax.servlet.http.HttpSession

@Controller
class DeadlockController : AbstractController() {

    private val lock1 = Any()
    private val lock2 = Any()
    private var switchFlag = true

    @RequestMapping(value = "/deadlock")
    fun process(ses: HttpSession, mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "deadlock")

        if (ses.getAttribute("dlpinit") == null) {
            ses.setAttribute("dlpinit", "true")
        } else {
            todoRemove()
        }

        /* Get threads that are in deadlock waiting */
        val bean = ManagementFactory.getThreadMXBean()
        val threadIds = bean.findDeadlockedThreads()
        if (threadIds != null) {
            mav.addObject("msg", msg?.getMessage("msg.dead.lock.detected", null, locale))
            val infos = bean.getThreadInfo(threadIds)
            mav.addObject("threadsInfo", infos)
        } else {
            mav.addObject("msg", msg?.getMessage("msg.dead.lock.not.occur", null, locale))
        }
        return mav
    }

    private fun todoRemove() {
        switchFlag = !switchFlag
        if (switchFlag) {
            lock12()
        } else {
            lock21()
        }
    }

    private fun lock12() {
        synchronized(lock1) {
            sleep()
            synchronized(lock2) {
                sleep()
            }
        }
    }

    private fun lock21() {
        synchronized(lock2) {
            sleep()
            synchronized(lock1) {
                sleep()
            }
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            log.error("Exception occurs: ", e)
        }

    }
}
