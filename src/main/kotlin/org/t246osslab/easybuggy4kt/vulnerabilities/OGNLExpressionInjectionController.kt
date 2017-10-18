package org.t246osslab.easybuggy4kt.vulnerabilities

import ognl.Ognl
import ognl.OgnlContext
import ognl.OgnlException
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.math.NumberUtils
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.util.*

@Controller
class OGNLExpressionInjectionController : AbstractController() {

    @RequestMapping(value = "/ognleijc")
    fun process(@RequestParam(value = "expression", required = false) expression: String?,
                mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "commandinjection")
        var value: Any? = null
        var errMessage = ""
        val ctx = OgnlContext()
        if (!StringUtils.isBlank(expression)) {
            try {
                val expr = Ognl.parseExpression(expression!!.replace("Math\\.".toRegex(), "@Math@"))
                value = Ognl.getValue(expr, ctx)
            } catch (e: OgnlException) {
                if (e.reason != null) {
                    errMessage = e.reason.message!!
                }
                log.debug("OgnlException occurs: ", e)
            } catch (e: Exception) {
                log.debug("Exception occurs: ", e)
            } catch (e: Error) {
                log.debug("Error occurs: ", e)
            }

        }
        if (expression != null) {
            mav.addObject("expression", expression)
            if (value == null) {
                mav.addObject("errmsg",
                        msg?.getMessage("msg.invalid.expression", arrayOf(errMessage), null, locale))
            }
        }
        if (value != null && NumberUtils.isNumber(value.toString())) {
            mav.addObject("value", value)
        }
        return mav
    }
}
