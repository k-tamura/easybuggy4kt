package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class OutOfMemoryErrorController2 {

    @RequestMapping(value = "/oome2")
    fun process(mav: ModelAndView) {
        mav.addObject("oome2", ByteArray(Integer.MAX_VALUE))
    }
}
