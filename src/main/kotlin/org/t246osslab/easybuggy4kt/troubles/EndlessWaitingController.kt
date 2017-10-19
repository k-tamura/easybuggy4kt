package org.t246osslab.easybuggy4kt.troubles

import org.apache.commons.lang.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.io.*
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class EndlessWaitingController : AbstractController() {

    @RequestMapping(value = "/endlesswaiting")
    @Throws(IOException::class)
    fun process(@RequestParam(value = "count", required = false) strCount: String?,
                req: HttpServletRequest, mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "endlesswaiting")
        val count = NumberUtils.toInt(strCount, 0)
        if (count > 0) {
            /* create a batch file in the temp directory */
            val batFile = createBatchFile(count,
                    req.session.servletContext.getAttribute("javax.servlet.context.tempdir").toString())

            if (batFile == null) {
                mav.addObject("errmsg", msg?.getMessage("msg.cant.create.batch", null, locale))
            } else {
                try {
                    /* execte the batch */
                    val pb = ProcessBuilder(batFile.absolutePath)
                    val process = pb.start()
                    process.waitFor()
                    mav.addObject("msg",
                            msg?.getMessage("msg.executed.batch", null, locale) + batFile.absolutePath)
                    mav.addObject("result",
                            printInputStream(process.inputStream) + printInputStream(process.errorStream))
                } catch (e: InterruptedException) {
                    log.error("InterruptedException occurs: ", e)
                    mav.addObject("errmsg",
                            msg?.getMessage("msg.unknown.exception.occur", arrayOf<String?>(e.message), null, locale))
                }

            }
        } else {
            mav.addObject("msg", msg?.getMessage("msg.enter.positive.number", null, locale))
        }
        return mav
    }

    private fun createBatchFile(count: Int, tmpdir: String): File? {

        val osName = System.getProperty("os.name").toLowerCase()
        var batFileName: String?
        var firstLine: String?
        if (osName.toLowerCase().startsWith("windows")) {
            batFileName = "test.bat"
            firstLine = "@echo off"
        } else {
            batFileName = "test.sh"
            firstLine = "#!/bin/sh"
        }

        var batFile: File?
        try {
            batFile = File(tmpdir, batFileName)
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            return null
        }

        try {
            FileWriter(batFile).use { fileWriter ->
                BufferedWriter(fileWriter).use { buffwriter ->
                    if (!batFile!!.setExecutable(true)) {
                        log.debug("batFile.setExecutable(true) returns false.")
                    }
                    buffwriter.write(firstLine!!)
                    buffwriter.newLine()

                    var i = 0
                    while (i < count && i < MAX_COUNT) {
                        if (i % 100 == 0) {
                            buffwriter.newLine()
                            buffwriter.write("echo ")
                        }
                        buffwriter.write((i % 10).toString())
                        i++
                    }
                    buffwriter.close()
                    fileWriter.close()
                    if (!osName.toLowerCase().startsWith("windows")) {
                        val runtime = Runtime.getRuntime()
                        runtime.exec("chmod 777 " + batFile!!.absolutePath)
                    }
                }
            }
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
        }

        return batFile
    }

    @Throws(IOException::class)
    private fun printInputStream(`is`: InputStream): String {
        val sb = StringBuilder()
        BufferedReader(InputStreamReader(`is`)).use { br ->
            while (true) {
                val line = br.readLine() ?: break
                sb.append(line + "<br>")
            }
        }
        return sb.toString()
    }

    companion object {

        private val MAX_COUNT = 100000
    }
}
