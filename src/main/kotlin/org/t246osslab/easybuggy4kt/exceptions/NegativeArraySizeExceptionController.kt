package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class NegativeArraySizeExceptionController {

    @RequestMapping(value = "/nase")
    fun process(mav: ModelAndView) {
        mav.addObject("nase", IntArray(-1))
    }
}
