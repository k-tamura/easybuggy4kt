package org.t246osslab.easybuggy4kt.vulnerabilities

import java.awt.image.BufferedImage
import java.awt.image.WritableRaster
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Arrays
import java.util.Locale

import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class UnrestrictedSizeUploadController : AbstractController() {

    @RequestMapping(value = "/ursupload", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "unrestrictedsizeupload")
        return mav
    }

    @RequestMapping(value = "/ursupload", headers = arrayOf("content-type=multipart/*"), method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    fun doPost(@RequestParam("file") file: MultipartFile, mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "unrestrictedsizeupload")

        // Get absolute path of the web application
        val appPath = req.servletContext.getRealPath("")

        // Create a directory to save the uploaded file if it does not exists
        val savePath = (appPath ?: System.getProperty("user.dir")) + File.separator + SAVE_DIR
        val fileSaveDir = File(savePath)
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir()
        }

        val fileName = file.originalFilename
        if (StringUtils.isBlank(fileName)) {
            return doGet(mav, req, res, locale)
        } else if (!isImageFile(fileName)) {
            mav.addObject("errmsg", msg?.getMessage("msg.not.image.file", null, locale))
            return doGet(mav, req, res, locale)
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

    @Throws(IOException::class)
    private fun writeFile(savePath: String, filePart: MultipartFile, fileName: String): Boolean {
        var isConverted = false
        try {
            FileOutputStream(savePath + File.separator + fileName).use { out ->
                filePart.inputStream.use { `in` ->
                    var read = 0
                    val bytes = ByteArray(1024)
                    while ((read = `in`.read(bytes)) != -1) {
                        out.write(bytes, 0, read)
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            // Ignore because file already exists
            isConverted = true
        }

        return isConverted
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
            ImageIO.write(image, "png", File(fileName))
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
