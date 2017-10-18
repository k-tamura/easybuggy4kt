package org.t246osslab.easybuggy4kt.troubles

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.OutputStreamWriter
import java.util.ArrayList
import java.util.Date
import java.util.Locale

import javax.servlet.http.HttpServletRequest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class FileDescriptorLeakController : AbstractController() {
    private var count: Long = 0

    @RequestMapping(value = "/filedescriptorleak")
    fun process(@RequestParam(value = "pingurl", required = false) pingURL: String?,
                req: HttpServletRequest, mav: ModelAndView, locale: Locale): ModelAndView {

        setViewAndCommonObjects(mav, locale, "filedescriptorleak")
        try {
            val file = File(req.servletContext.getAttribute("javax.servlet.context.tempdir").toString(),
                    "history.csv")
            val fos = FileOutputStream(file, true)
            val osw = OutputStreamWriter(fos).apply {
                write(Date().toString() + ",")
                write(req.remoteAddr + ",")
                write(req.requestedSessionId)
                write(System.getProperty("line.separator"))
                flush()
            }
            count++

            val history = ArrayList<Array<String>>()
            val br = BufferedReader(FileReader(file))
            val headerLength = 0
            var line: String = br.readLine()
            var currentLineNum: Long = 0
            while (line != null) {
                line = br.readLine()
                if (count - currentLineNum <= MAX_DISPLAY_COUNT) {
                    history.add(headerLength, line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                }
                currentLineNum++
            }
            mav.addObject("history", history)

        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            mav.addObject("errmsg",
                    msg?.getMessage("msg.unknown.exception.occur", arrayOf<String?>(e.message), null, locale))
        }

        return mav
    }

    companion object {

        private val MAX_DISPLAY_COUNT = 15
    }
}
