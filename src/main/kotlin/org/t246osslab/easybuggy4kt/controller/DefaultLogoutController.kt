package org.t246osslab.easybuggy4kt.controller

import javax.servlet.http.HttpSession

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class DefaultLogoutController {

    @RequestMapping(value = "/logout")
    fun process(ses: HttpSession): String {

        ses.invalidate()
        return "redirect:/"
    }
}
