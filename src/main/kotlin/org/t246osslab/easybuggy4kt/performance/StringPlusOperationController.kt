package org.t246osslab.easybuggy4kt.performance

import org.apache.commons.lang3.math.NumberUtils
import org.owasp.esapi.ESAPI
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.util.*

@Controller
class StringPlusOperationController : AbstractController() {

    @RequestMapping(value = "/strplusopr")
    fun process(@RequestParam(value = "length", required = false) strLength: String?,
                @RequestParam(value = "characters", required = false) characters: Array<String>?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "strplusopr")
        try {
            val length = NumberUtils.toInt(strLength, 0)
            val html = createMainContent(characters, mav, locale, length)
            mav.addObject("html", html.toString())
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
        }

        return mav
    }

    private fun createMainContent(characters: Array<String>?, mav: ModelAndView, locale: Locale, length: Int): StringBuilder {
        val html = StringBuilder()
        html.append("<p>" + msg?.getMessage("label.available.characters", null, locale) + "</p>")

        appendCheckBox(characters, locale, html, ALL_NUMBERS, "label.numbers")
        appendCheckBox(characters, locale, html, ALL_UPPER_CHARACTERS, "label.uppercase.characters")
        appendCheckBox(characters, locale, html, ALL_LOWER_CHARACTERS, "label.lowercase.characters")
        appendCheckBox(characters, locale, html, ALL_SIGNS, "label.signs")

        html.append("<input type=\"submit\" value=\"" + msg?.getMessage("label.submit", null, locale) + "\">")
        html.append("<br /><br />")

        if (length > 0) {
            mav.addObject("length", length)
            var s = ""
            if (characters != null) {
                val rand = java.util.Random()
                log.info("Start Date: {}", Date())
                var i = 0
                while (i < length && i < MAX_LENGTH) {
                    s += characters[rand.nextInt(characters.size)]
                    i++
                }
                log.info("End Date: {}", Date())
            }
            html.append(msg?.getMessage("label.execution.result", null, locale))
            html.append("<br /><br />")
            html.append(encodeForHTML(s))
        } else {
            html.append(msg?.getMessage("msg.enter.positive.number", null, locale))
        }
        return html
    }

    private fun appendCheckBox(characters: Array<String>?, locale: Locale, message: StringBuilder, allCharacters: Array<String>,
                               label: String) {
        message.append("<p>" + msg?.getMessage(label, null, locale) + "</p>")
        message.append("<p>")
        for (i in allCharacters.indices) {
            message.append("<input type=\"checkbox\" name=\"characters\" value=\"")
            message.append(allCharacters[i])
            if (characters == null || Arrays.asList(*characters).contains(allCharacters[i])) {
                message.append("\" checked=\"checked\">")
            } else {
                message.append("\">")
            }
            message.append(allCharacters[i])
            message.append(" ")
        }
        message.append("</p>")
    }

    companion object {

        private val MAX_LENGTH = 1000000
        private val ALL_NUMBERS = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        private val ALL_UPPER_CHARACTERS = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        private val ALL_LOWER_CHARACTERS = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
        private val ALL_SIGNS = arrayOf("!", "#", "$", "%", "&", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "@", "[", "]", "^", "_", "{", "|", "}")
    }
}
