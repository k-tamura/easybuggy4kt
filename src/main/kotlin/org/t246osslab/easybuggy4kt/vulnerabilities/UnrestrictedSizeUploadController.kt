package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.io.FilenameUtils
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
class UnrestrictedSizeUploadController : AbstractController() {

    @RequestMapping(value = "/ursupload", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "unrestrictedsizeupload")
        return mav
    }

    @RequestMapping(value = "/ursupload", headers = arrayOf("content-type=multipart/*"), method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    fun doPost(@RequestParam("file") file: MultipartFile, mav: ModelAndView, req: HttpServletRequest, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "unrestrictedsizeupload")

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
            return doGet(mav, locale)
        } else if (!isImageFile(fileName)) {
            mav.addObject("errmsg", msg?.getMessage("msg.not.image.file", null, locale))
            return doGet(mav, locale)
        }
        var isConverted = writeFile(savePath, file, fileName)

        if (!isConverted) {
            isConverted = reverseColor(File(savePath + File.separator + fileName).absolutePath)
        }

        if (isConverted) {
            mav.addObject("msg", msg?.getMessage("msg.reverse.color.complete", null, locale))
            mav.addObject("upladFilePath", SAVE_DIR + "/" + fileName)
        } else {
            mav.addObject("errmsg", msg?.getMessage("msg.reverse.color.fail", null, locale))
            mav.addObject("note", msg?.getMessage("msg.note.unrestrictedsizeupload", null, locale))
        }
        return mav
    }

    private fun isImageFile(fileName: String): Boolean {
        return Arrays.asList("png", "gif", "jpg", "jpeg", "tif", "tiff", "bmp").contains(
                FilenameUtils.getExtension(fileName))
    }

    // Reverse the color of the image file
    @Throws(IOException::class)
    private fun reverseColor(fileName: String): Boolean {
        var isConverted = false
        try {
            val image = ImageIO.read(File(fileName))
            val raster = image.raster
            val pixelBuffer = IntArray(raster.numDataElements)
            for (y in 0 until raster.height) {
                for (x in 0 until raster.width) {
                    raster.getPixel(x, y, pixelBuffer)
                    pixelBuffer[0] = pixelBuffer[0].inv()
                    pixelBuffer[1] = pixelBuffer[1].inv()
                    pixelBuffer[2] = pixelBuffer[2].inv()
                    raster.setPixel(x, y, pixelBuffer)
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
