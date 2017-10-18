package org.t246osslab.easybuggy4kt.performance

import java.util.Locale

import org.apache.commons.lang3.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class CreatingUnnecessaryObjectsController : AbstractController() {

    @RequestMapping(value = "/createobjects")
    fun process(@RequestParam(value = "number", required = false) strNumber: String?, mav: ModelAndView,
                locale: Locale): ModelAndView {
        val number = NumberUtils.toInt(strNumber, -1)
        val message = StringBuilder()
        setViewAndCommonObjects(mav, locale, "createobjects")

        if (number > 0) {
            mav.addObject("number", number)
            when (number) {
                1 -> {
                }
                2 -> message.append("1 + 2 = ")
                3 -> message.append("1 + 2 + 3 = ")
                4 -> message.append("1 + 2 + 3 + 4 = ")
                5 -> message.append("1 + 2 + 3 + 4 + 5 = ")
                else -> {
                    message.append("1 + 2 + 3 + ... + $number = ")
                    message.append("\\(\\begin{eqnarray}\\sum_{ k = 1 }^{ $number } k\\end{eqnarray}\\) = ")
                }
            }
        } else {
            message.append("1 + 2 + 3 + ... + n = ")
            message.append("\\(\\begin{eqnarray}\\sum_{ k = 1 }^{ n } k\\end{eqnarray}\\) = ")
        }
        if (number >= 1) {
            val start = System.nanoTime()
            message.append(calcSum1(number))
            log.info("{} ms", (System.nanoTime() - start) / 1000000f)
        }
        mav.addObject("msg", message.toString())
        return mav
    }

    private fun calcSum1(number: Int): Long? {
        var sum: Long? = 0L
        for (i in 1..number) {
            sum=sum!!.plus(i)
        }
        return sum
    }
    /*
    private long calcSum2(int number) {
        long sum = 0L;
        for (int i = 1; i <= number; i++) {
            sum += i;
        }
        return sum;
    }

    private long calcSum3(int number) {
        return (long) number * (number + 1) / 2;
	}
	 */
}