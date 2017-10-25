package org.t246osslab.easybuggy4kt.performance

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.util.*
import java.util.regex.Pattern

@Controller
class SlowRegularExpressionController : AbstractController() {

    @RequestMapping(value = "/slowre")
    fun process(@RequestParam(value = "word", required = false) word: String?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        val message: String? = if (!StringUtils.isBlank(word)) {
            if (isMatched(word!!)) {
                msg?.getMessage("msg.match.regular.expression", null, locale)
            } else {
                msg?.getMessage("msg.not.match.regular.expression", null, locale)
            }
        } else {
            msg?.getMessage("msg.enter.string", null, locale)
        }
        setViewAndCommonObjects(mav, locale, "slowregex")
        mav.addObject("msg", message)
        return mav
    }

    private fun isMatched(word: String): Boolean {
        log.info("Start Date: {}", Date())
        val compile = Pattern.compile("^([a-z0-9]+[-]{0,1}){1,100}$")
        val matcher = compile.matcher(word)
        val matches = matcher.matches()
        log.info("End Date: {}", Date())
        return matches
    }
}