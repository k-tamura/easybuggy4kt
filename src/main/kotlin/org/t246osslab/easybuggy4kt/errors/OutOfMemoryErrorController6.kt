package org.t246osslab.easybuggy4kt.errors

import java.nio.ByteBuffer

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class OutOfMemoryErrorController6 {

    @RequestMapping(value = "/oome6")
    fun process(mav: ModelAndView) {
        mav.addObject("oome6", ByteBuffer.allocateDirect(99999999))
    }
}
