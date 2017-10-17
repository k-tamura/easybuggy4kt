package org.t246osslab.easybuggy4kt.vulnerabilities

import java.util.Locale

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class XSSController : AbstractController() {

    @RequestMapping(value = "/xss")
    fun process(@RequestParam(value = "string", required = false) string: String, mav: ModelAndView,
                locale: Locale): ModelAndView {
        mav.viewName = "xss"
        mav.addObject("title", msg?.getMessage("title.xss.page", null, locale))
        if (!StringUtils.isBlank(string)) {
            // Reverse the given string
            val reversedName = StringUtils.reverse(string)
            mav.addObject("msg", msg?.getMessage("label.reversed.string", null, locale) + " : " + reversedName)
        } else {
            mav.addObject("msg", msg?.getMessage("msg.enter.string", null, locale))
        }
        return mav
    }
}