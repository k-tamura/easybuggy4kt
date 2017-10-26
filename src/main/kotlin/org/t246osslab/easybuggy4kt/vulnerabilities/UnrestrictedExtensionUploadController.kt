package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import org.t246osslab.easybuggy4kt.core.utils.MultiPartFileUtils.Companion.writeFile
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO
import javax.imageio.ImageIO.write
import javax.servlet.http.HttpServletRequest

@Controller
class UnrestrictedExtensionUploadController : AbstractController() {

    @RequestMapping(value = "/ureupload", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, req: HttpServletRequest, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "unrestrictedextupload")
        if (req.getAttribute("errorMessage") != null) {
            mav.addObject("errmsg", req.getAttribute("errorMessage"))
        }
        return mav
    }

    @RequestMapping(value = "/ureupload", headers = arrayOf("content-type=multipart/*"), method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    fun doPost(@RequestParam("file") file: MultipartFile, mav: ModelAndView, req: HttpServletRequest, locale: Locale): ModelAndView {

        if (req.getAttribute("errorMessage") != null) {
            return doGet(mav, req, locale)
        }

        setViewAndCommonObjects(mav, locale, "unrestrictedextupload")
        // Get absolute path of the web application
        val appPath = req.session.servletContext.getRealPath("")

        // Create a directory to save the uploaded file if it does not exists
        val savePath = (appPath ?: System.getProperty("user.dir")) + File.separator + SAVE_DIR
        val fileSaveDir = File(savePath)
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir()
        }

        val fileName = file.originalFilename
        if (StringUtils.isBlank(fileName)) {
            return doGet(mav, req, locale)
        }
        var isConverted = writeFile(savePath, file, fileName)

        if (!isConverted) {
            isConverted = convert2GrayScale(File(savePath + File.separator + fileName).absolutePath)
        }

        if (isConverted) {
            mav.addObject("msg", msg?.getMessage("msg.convert.grayscale.complete", null, locale))
            mav.addObject("upladFilePath", SAVE_DIR + "/" + fileName)
        } else {
            mav.addObject("note", msg?.getMessage("msg.note.unrestrictedextupload", null, locale))
            mav.addObject("errmsg", msg?.getMessage("msg.convert.grayscale.fail", null, locale))
        }
        return mav
    }

    // Convert color image into gray scale image.
    @Throws(IOException::class)
    private fun convert2GrayScale(fileName: String): Boolean {
        var isConverted = false
        try {
            // Convert the file into gray scale image.
            val image = ImageIO.read(File(fileName))
            if (image == null) {
                log.warn("Cannot read upload file as image file, file name: {}", fileName)
                return false
            }

            // convert to gray scale
            for (y in 0 until image.height) {
                for (x in 0 until image.width) {
                    var p = image.getRGB(x, y)
                    val a = p shr 24 and 0xff
                    val r = p shr 16 and 0xff
                    val g = p shr 8 and 0xff
                    val b = p and 0xff

                    // calculate average
                    val avg = (r + g + b) / 3

                    // replace RGB value with avg
                    p = a shl 24 or (avg shl 16) or (avg shl 8) or avg

                    image.setRGB(x, y, p)
                }
            }
            // Output the image
            write(image, "png", File(fileName))
            isConverted = true
        } catch (e: Exception) {
            // Log and ignore the exception
            log.warn("Exception occurs: ", e)
        }

        return isConverted
    }

    companion object {

        // Name of the directory where uploaded files is saved
        private val SAVE_DIR = "uploadFiles"
    }
}
