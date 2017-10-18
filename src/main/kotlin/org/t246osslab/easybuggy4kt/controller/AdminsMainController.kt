package org.t246osslab.easybuggy4kt.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
class AdminsMainController : AbstractController() {

    @RequestMapping(value = "/admins/main")
    fun doGet(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "adminmain")
        return mav
    }
}
