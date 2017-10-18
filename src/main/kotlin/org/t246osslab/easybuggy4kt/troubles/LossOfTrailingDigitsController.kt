package org.t246osslab.easybuggy4kt.troubles

import org.apache.commons.lang.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.util.*

@Controller
class LossOfTrailingDigitsController : AbstractController() {

    @RequestMapping(value = "/lotd")
    fun process(@RequestParam(value = "number", required = false) strNumber: String?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "lossoftrailingdigits")
        val number = NumberUtils.toDouble(strNumber, -1.0)
        if (!java.lang.Double.isNaN(number) && -1 < number && number < 1) {
            mav.addObject("number", strNumber)
            mav.addObject("result", number + 1)
        }
        return mav
    }
}
