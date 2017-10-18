package org.t246osslab.easybuggy4kt.troubles

import java.util.Locale

import org.apache.commons.lang.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class RoundOffErrorController : AbstractController() {

    @RequestMapping(value = "/roe")
    fun process(@RequestParam(value = "number", required = false) strNumber: String?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "roundofferror")
        val number = NumberUtils.toDouble(strNumber, -1.0)
        if (1 <= number && number <= 9) {
            mav.addObject("number", strNumber)
            mav.addObject("result", number - 0.9)
        }
        return mav
    }
}
