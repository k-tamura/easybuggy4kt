package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.io.IOUtils
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import org.t246osslab.easybuggy4kt.core.model.User
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.XMLConstants
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

@Controller
class XEEandXXEController : AbstractController() {

    @Autowired
    internal var jdbcTemplate: JdbcTemplate? = null

    @RequestMapping(value = *arrayOf("/xee", "/xxe"), method = arrayOf(RequestMethod.GET))
    @Throws(IOException::class)
    fun doGet(mav: ModelAndView, req: HttpServletRequest, res: HttpServletResponse, locale: Locale): ModelAndView {

        var resource: Resource = ClassPathResource("/xml/sample_users.xml")
        mav.addObject("sample_users_xml", IOUtils.toString(resource.inputStream))
        if ("/xee" == req.servletPath) {
            setViewAndCommonObjects(mav, locale, "xee")
            resource = ClassPathResource("/xml/xee.xml")
            mav.addObject("xee_xml", IOUtils.toString(resource.getInputStream()))
        } else {
            setViewAndCommonObjects(mav, locale, "xxe")
            resource = ClassPathResource("/xml/xxe.xml")
            mav.addObject("xxe_xml", IOUtils.toString(resource.getInputStream()))
            resource = ClassPathResource("/xml/xxe.dtd")
            mav.addObject("xxe_dtd", IOUtils.toString(resource.getInputStream()))
        }
        if (req.getAttribute("errorMessage") != null) {
            mav.addObject("errmsg", req.getAttribute("errorMessage"))
        }
        return mav
    }

    @RequestMapping(value = *arrayOf("/xee", "/xxe"), headers = arrayOf("content-type=multipart/*"), method = arrayOf(RequestMethod.POST))
    @Throws(IOException::class)
    fun doPost(@RequestParam("file") file: MultipartFile, mav: ModelAndView, req: HttpServletRequest,
               res: HttpServletResponse, locale: Locale): ModelAndView {

        if (req.getAttribute("errorMessage") != null) {
            return doGet(mav, req, res, locale)
        }

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
        } else if (!fileName.endsWith(".xml")) {
            mav.addObject("errmsg", msg?.getMessage("msg.not.xml.file", null, locale))
            return doGet(mav, req, res, locale)
        }
        var isRegistered = writeFile(savePath, file, fileName)

        val customHandler = CustomHandler()
        customHandler.setLocale(locale)
        val parser: SAXParser
        try {
            val spf = SAXParserFactory.newInstance()
            if ("/xee" == req.servletPath) {
                customHandler.setInsert(true)
                spf.setFeature("http://xml.org/sax/features/external-general-entities", false)
                spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
            } else {
                spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
            }
            parser = spf.newSAXParser()
            parser.parse(File(savePath + File.separator + fileName).absolutePath, customHandler)
            isRegistered = true
        } catch (e: ParserConfigurationException) {
            log.error("ParserConfigurationException occurs: ", e)
        } catch (e: SAXException) {
            log.error("SAXException occurs: ", e)
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
        }

        if ("/xee" == req.servletPath) {
            if (isRegistered && customHandler.isRegistered) {
                mav.addObject("msg", msg?.getMessage("msg.batch.registration.complete", null, locale))
            } else {
                mav.addObject("errmsg", msg?.getMessage("msg.batch.registration.fail", null, locale))
            }
            setViewAndCommonObjects(mav, locale, "xee")
        } else {
            if (isRegistered && customHandler.isRegistered) {
                mav.addObject("msg", msg?.getMessage("msg.batch.update.complete", null, locale))
            } else {
                mav.addObject("errmsg", msg?.getMessage("msg.batch.update.fail", null, locale))
            }
            setViewAndCommonObjects(mav, locale, "xxe")
        }
        mav.addObject("resultList", customHandler.result)
        return mav
    }

    @Throws(IOException::class)
    private fun writeFile(savePath: String, filePart: MultipartFile, fileName: String): Boolean {
        var isRegistered = false
        try {
            FileOutputStream(savePath + File.separator + fileName).use { out ->
                filePart.inputStream.use { `in` ->
                    val bytes = ByteArray(1024)
                    var read = `in`.read(bytes)
                    while (read != -1) {
                        out.write(bytes, 0, read)
                        read = `in`.read(bytes)
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            // Ignore because file already exists
            isRegistered = true
        }

        return isRegistered
    }

    inner class CustomHandler : DefaultHandler() {
        internal var result = ArrayList<Any>()
        internal var isRegistered = false
            private set
        private var isUsersExist = false
        private var isInsert = false
        private var locale: Locale? = null

        @Throws(SAXException::class)
        override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
            if ("users" == qName) {
                isUsersExist = true

            } else if (isUsersExist && "user" == qName) {
                val executeResult = upsertUser(attributes, locale)
                val user = User()
                if (executeResult == null) {
                    user.userId = attributes!!.getValue("uid")
                    user.name = attributes.getValue("name")
                    user.password = attributes.getValue("password")
                    user.phone = attributes.getValue("phone")
                    user.mail = attributes.getValue("mail")
                    result.add(user)
                } else {
                    result.add(attributes!!.getValue("uid") + " :: " + executeResult)
                }
                isRegistered = true
            }
        }

        internal fun setInsert(isInsert: Boolean) {
            this.isInsert = isInsert
        }

        internal fun setLocale(locale: Locale) {
            this.locale = locale
        }

        fun upsertUser(attributes: Attributes?, locale: Locale?): String? {
            var resultMessage: String? = null
            try {
                val count = jdbcTemplate!!.queryForObject(
                        "select count(*) from users where id = ?", Int::class.java, attributes!!.getValue("uid"))

                if (count == 1) {
                    if (isInsert) {
                        return msg?.getMessage("msg.user.already.exist", null, locale)
                    }
                } else {
                    if (!isInsert) {
                        return msg?.getMessage("msg.user.not.exist", null, locale)
                    }
                }
                if (isInsert) {
                    val insertCount = jdbcTemplate!!.update("insert into users values (?, ?, ?, ?, ?, ?, ?)",
                            attributes.getValue("uid"), attributes.getValue("name"), attributes.getValue("password"),
                            RandomStringUtils.randomNumeric(10), "true", attributes.getValue("phone"),
                            attributes.getValue("mail"))
                    if (insertCount != 1) {
                        return msg?.getMessage("msg.user.already.exist", null, locale)
                    }
                } else {
                    val updateCount = jdbcTemplate!!.update(
                            "update users set name = ?, password = ?, phone = ?, mail = ? where id = ?",
                            attributes.getValue("name"), attributes.getValue("password"), attributes.getValue("phone"),
                            attributes.getValue("mail"), attributes.getValue("uid"))
                    if (updateCount != 1) {
                        return msg?.getMessage("msg.user.not.exist", null, locale)
                    }
                }
            } catch (e: DataAccessException) {
                resultMessage = msg?.getMessage("msg.db.access.error.occur", arrayOf<String?>(e.message), locale)
                log.error("DataAccessException occurs: ", e)
            } catch (e: Exception) {
                resultMessage = msg?.getMessage("msg.unknown.exception.occur", arrayOf<String?>(e.message), locale)
                log.error("Exception occurs: ", e)
            }

            return resultMessage
        }
    }

    companion object {

        // Name of the directory where uploaded files is saved
        private val SAVE_DIR = "uploadFiles"
    }
}
