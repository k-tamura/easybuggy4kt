package org.t246osslab.easybuggy4kt.exceptions

import java.util.ArrayList

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class IllegalArgumentExceptionController {

    @RequestMapping(value = "/iae")
    fun process(mav: ModelAndView) {
        mav.addObject(ArrayList<Any>(-1))
    }
}
