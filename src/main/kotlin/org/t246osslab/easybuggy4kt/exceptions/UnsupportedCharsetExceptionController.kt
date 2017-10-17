package org.t246osslab.easybuggy4kt.exceptions

import java.nio.charset.Charset

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class UnsupportedCharsetExceptionController {

    @RequestMapping(value = "/uce")
    fun process(mav: ModelAndView) {
        mav.addObject("uce", String("str".toByteArray(Charset.defaultCharset()), Charset.forName("test")))
    }
}
