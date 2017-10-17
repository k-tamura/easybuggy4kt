package org.t246osslab.easybuggy4kt.troubles

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.lang.management.ManagementFactory
import java.util.*

@Controller
class ThreadLeakController : AbstractController() {

    @RequestMapping(value = "/threadleak")
    fun process(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "threadleak")
        val sub = ThreadCountLoggingThread()
        sub.start()
        val bean = ManagementFactory.getThreadMXBean()
        mav.addObject("count", bean.allThreadIds.size)
        return mav
    }
}

internal class ThreadCountLoggingThread : Thread() {

    override fun run() {
        while (true) {
            try {
                Thread.sleep(100000)
                val bean = ManagementFactory.getThreadMXBean()
                log.info("Current thread count: {}", bean.allThreadIds.size)
            } catch (e: InterruptedException) {
                // ignore
            }

        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(ThreadCountLoggingThread::class.java)
    }
}
