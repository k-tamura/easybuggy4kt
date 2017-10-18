package org.t246osslab.easybuggy4kt.troubles

import java.util.Locale

import org.apache.commons.lang.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class TruncationErrorController : AbstractController() {

    @RequestMapping(value = "/te")
    fun process(@RequestParam(value = "number", required = false) strNumber: String?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "truncationerror")

        val number = NumberUtils.toDouble(strNumber, -1.0)
        if (0 < number && number < 10) {
            mav.addObject("number", strNumber)
            mav.addObject("result", 10.0 / number)
        }
        return mav
    }
}
