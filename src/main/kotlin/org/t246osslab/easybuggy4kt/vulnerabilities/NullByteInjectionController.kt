package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang3.StringUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Controller
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class NullByteInjectionController : AbstractController() {

    @RequestMapping(value = "/nullbyteijct")
    @Throws(IOException::class)
    fun process(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView? {
        setViewAndCommonObjects(mav, locale, "nullbyteinjection")
        var fileName = req.getParameter("fileName")
        if (StringUtils.isBlank(fileName)) {
            return mav
        } else {
            fileName = fileName + ".pdf"
        }
        val resource = ClassPathResource("/pdf/" + fileName)
        try {
            resource.inputStream.use { fis ->
                res.outputStream.use { os ->
                    val mimeType = req.session.servletContext.getMimeType(resource.uri.path)
                    res.contentType = mimeType ?: "application/octet-stream"
                    res.setContentLength(resource.contentLength().toInt())
                    res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    FileCopyUtils.copy(fis, os)
                }
            }
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
        }

        return null
    }
}
