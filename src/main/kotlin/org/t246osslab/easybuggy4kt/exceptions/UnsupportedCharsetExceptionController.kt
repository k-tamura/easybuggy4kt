package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.nio.charset.Charset

@Controller
class UnsupportedCharsetExceptionController {

    @RequestMapping(value = "/uce")
    fun process(mav: ModelAndView) {
        mav.addObject("uce", String("str".toByteArray(Charset.defaultCharset()), Charset.forName("test")))
    }
}
