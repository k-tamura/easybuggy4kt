package org.t246osslab.easybuggy4kt.vulnerabilities

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.util.*

@Controller
class UnintendedFileDisclosureController : AbstractController() {

    @RequestMapping(value = "/clientinfo")
    fun clientinfo(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "clientinfo")
        return mav
    }

    @RequestMapping(value = "/serverinfo")
    fun serverinfo(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "serverinfo")
        mav.addObject("properties", System.getProperties())
        return mav
    }
}