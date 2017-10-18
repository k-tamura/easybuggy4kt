package org.t246osslab.easybuggy4kt.troubles

import org.apache.commons.lang.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.math.BigDecimal
import java.util.*

@Controller
class IntegerOverflowController : AbstractController() {

    @RequestMapping(value = "/iof")
    fun process(@RequestParam(value = "times", required = false) strTimes: String?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "intoverflow")
        var thickness: BigDecimal? = null
        var thicknessM: BigDecimal? = null
        var thicknessKm: BigDecimal? = null
        val times = NumberUtils.toInt(strTimes, -1)
        if (strTimes != null) {
            var multipleNumber: Long = 1
            if (times >= 0) {
                for (i in 0 until times) {
                    multipleNumber = multipleNumber * 2
                }
                thickness = BigDecimal(multipleNumber).divide(BigDecimal(10)) // mm
                thicknessM = thickness!!.divide(BigDecimal(1000)) // m
                thicknessKm = thicknessM!!.divide(BigDecimal(1000)) // km
            }
        }
        if (times >= 0) {
            mav.addObject("times", strTimes)
            val description = StringBuilder()
            description.append(thickness.toString()!! + " mm")
            if (thicknessM != null && thicknessKm != null) {
                description.append(
                        if (thicknessM.toInt() >= 1 && thicknessKm.toInt() < 1) " = $thicknessM m" else "")
                description.append(if (thicknessKm.toInt() >= 1) " = $thicknessKm km" else "")
            }
            if (times == 42) {
                description.append(" : " + msg?.getMessage("msg.answer.is.correct", null, locale))
            }
            mav.addObject("description", description.toString())
        }
        return mav
    }
}
