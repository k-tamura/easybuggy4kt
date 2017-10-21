package org.t246osslab.easybuggy4kt.vulnerabilities

import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.util.*
import javax.script.ScriptEngineManager
import javax.script.ScriptException

@Controller
class CodeInjectionController : AbstractController() {

    @RequestMapping(value = "/codeijc")
    fun process(@RequestParam(value = "jsonString", required = false) jsonString: String?,
                mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "codeinjection")
        if (!StringUtils.isBlank(jsonString)) {
            parseJson(jsonString!!, mav, locale)
        } else {
            mav.addObject("msg", msg?.getMessage("msg.enter.json.string", null, locale))
        }
        return mav
    }

    private fun parseJson(jsonString: String, mav: ModelAndView, locale: Locale) {
        /* Remove spaces and line breaks to parse as JSON */
        var convertedJsonString = jsonString.replace(" ".toRegex(), "")
        convertedJsonString = convertedJsonString.replace("\r\n".toRegex(), "")
        convertedJsonString = convertedJsonString.replace("\n".toRegex(), "")
        try {
            /* Parse the input string as JSON */
            val manager = ScriptEngineManager()
            val scriptEngine = manager.getEngineByName("JavaScript")
            scriptEngine.eval("JSON.parse('$convertedJsonString')")
            mav.addObject("msg", msg?.getMessage("msg.valid.json", null, locale))
        } catch (e: ScriptException) {
            mav.addObject("errmsg", msg?.getMessage("msg.invalid.json",
                    arrayOf(e.message), null, locale))
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            mav.addObject("errmsg", msg?.getMessage("msg.invalid.json",
                    arrayOf(e.message), null, locale))
        }

    }
}
