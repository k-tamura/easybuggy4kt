package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.mail.MessagingException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.Part

/**
 * A servlet that takes message details from user and send it as a new mail
 * through an SMTP server. The mail may contain a attachment which is the file
 * uploaded from client.
 */
@Controller
class MailHeaderInjectionController : AbstractController() {

    @Value("\${spring.mail.username}")
    internal var username: String? = null

    @Value("\${spring.mail.password}")
    internal var password: String? = null

    // administrator's mail address
    @Value("\${mail.admin.address}")
    private var adminAddress: String? = null

    @Autowired
    private val javaMailSender: JavaMailSender? = null

    private val isReadyToSendEmail: Boolean
        get() = !(StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(adminAddress))

    @RequestMapping(value = "/mailheaderijct", method = arrayOf(RequestMethod.GET))
    fun doGet(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "mailheaderinjection")
        if (isReadyToSendEmail) {
            mav.addObject("isReady", "yes")
        } else {
            mav.addObject("note", msg?.getMessage("msg.smtp.server.not.setup", null, locale))
        }
        return mav
    }

    @RequestMapping(value = "/mailheaderijct", method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class, ServletException::class)
    fun doPost(mav: ModelAndView, req: HttpServletRequest, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "mailheaderinjection")

        val uploadedFiles = saveUploadedFiles(req)

        val name = req.getParameter("name")
        val mail = req.getParameter("mail")
        val subject = req.getParameter("subject")
        val content = req.getParameter("content")
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(content)) {
            mav.addObject("errmsg", msg?.getMessage("msg.mail.is.empty", null, locale))
            return doGet(mav, locale)
        }
        val sb = StringBuilder()
        sb.append(msg?.getMessage("label.name", null, locale)).append(": ").append(name).append("<br>")
        sb.append(msg?.getMessage("label.mail", null, locale)).append(": ").append(mail).append("<br>").append("<br>")
        sb.append(msg?.getMessage("label.content", null, locale)).append(": ").append(content).append("<br>")
        try {
            sendMail(subject, sb.toString(), uploadedFiles)
            mav.addObject("msg", msg?.getMessage("msg.sent.mail", null, locale))
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            mav.addObject("errmsg",
                    msg?.getMessage("msg.unknown.exception.occur", arrayOf(e.message), null, locale))
        } finally {
            deleteUploadFiles(uploadedFiles)
        }
        return doGet(mav, locale)
    }

    @Throws(MessagingException::class)
    private fun sendMail(subject: String, text: String, uploadedFiles: List<File>) {
        val message = javaMailSender!!.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setText(text)
        helper.setSubject(subject)
        helper.setTo(adminAddress!!)
        for (uploadedFile in uploadedFiles) {
            helper.addAttachment(uploadedFile.name, uploadedFile)
        }
        javaMailSender!!.send(message)
    }

    /**
     * Saves files uploaded from the client and return a list of these files
     * which will be attached to the mail message.
     */
    @Throws(IOException::class, ServletException::class)
    private fun saveUploadedFiles(request: HttpServletRequest): List<File> {
        val listFiles = ArrayList<File>()
        val buffer = ByteArray(4096)
        var bytesRead: Int
        val multiparts = request.parts
        if (!multiparts.isEmpty()) {
            for (part in request.parts) {
                // creates a file to be saved
                val fileName = extractFileName(part)
                if (StringUtils.isBlank(fileName)) {
                    // not attachment part, continue
                    continue
                }

                val saveFile = File(fileName!!)
                log.debug("Uploaded file is saved on: " + saveFile.absolutePath)

                try {
                    FileOutputStream(saveFile).use { outputStream ->
                        part.inputStream.use {
                            bytesRead = it.read(buffer)
                            // saves uploaded file
                            while (bytesRead != -1) {
                                bytesRead = it.read(buffer)
                                outputStream.write(buffer, 0, bytesRead)
                            }
                        }
                    }
                } catch (e: Exception) {
                    log.error("Exception occurs: ", e)
                }

                listFiles.add(saveFile)
            }
        }
        return listFiles
    }

    /**
     * Retrieves file name of a upload part from its HTTP header
     */
    private fun extractFileName(part: Part): String? {
        val contentDisp = part.getHeader("content-disposition")
        val items = contentDisp.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return items
                .firstOrNull { s -> s.trim { it <= ' ' }.startsWith("filename") }
                ?.let { it.substring(it.indexOf('=') + 2, it.length - 1) }
    }

    /**
     * Deletes all uploaded files, should be called after the e-mail was sent.
     */
    private fun deleteUploadFiles(listFiles: List<File>?) {
        if (listFiles != null && !listFiles.isEmpty()) {
            listFiles
                    .filterNot { it.delete() }
                    .forEach { log.debug("Cannot remove file: {}", it) }
        }
    }
}
