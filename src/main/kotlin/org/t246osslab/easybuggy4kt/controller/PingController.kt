package org.t246osslab.easybuggy4kt.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class PingController {

    @RequestMapping(value = "/ping")
    fun hello(mav: ModelAndView): ModelAndView {
        mav.viewName = "ping"
        return mav
    }
}

