package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class ArithmeticExceptionController {

    @RequestMapping(value = "/ae")
    fun process(mav: ModelAndView) {
        mav.addObject(1 / 0)
    }
}