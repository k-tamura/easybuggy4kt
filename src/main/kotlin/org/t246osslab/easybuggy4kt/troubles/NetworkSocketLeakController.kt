package org.t246osslab.easybuggy4kt.troubles

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class NetworkSocketLeakController : AbstractController() {

    @RequestMapping(value = "/netsocketleak")
    fun process(mav: ModelAndView, req: HttpServletRequest, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "netsocketleak")
        val connection: HttpURLConnection?
        val url: URL?
        var pingURL: String? = req.getParameter("pingurl")
        try {
            if (pingURL == null) {
                pingURL = req.scheme + "://" + req.serverName + ":" + req.serverPort + "/ping"
            }
            url = URL(pingURL)
            val start = System.currentTimeMillis()
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val responseCode = connection.responseCode
            val end = System.currentTimeMillis()

            mav.addObject("pingURL", pingURL)
            mav.addObject("responseCode", responseCode)
            mav.addObject("responseTime", end - start)

        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            mav.addObject("errmsg",
                    msg?.getMessage("msg.unknown.exception.occur", arrayOf(e.message), null, locale))
        }

        return mav
    }
}
