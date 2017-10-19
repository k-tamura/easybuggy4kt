package org.t246osslab.easybuggy4kt.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpSession

@Controller
class IndexController : AbstractController() {

    @RequestMapping(value = "/")
    fun init(ses: HttpSession, mav: ModelAndView, locale: Locale): ModelAndView {
        ses.removeAttribute("dlpinit")
        setViewAndCommonObjects(mav, locale, "index")
        mav.addObject("title", "EasyBuggy Bootlin")
        var permName: String?
        var lblPerm: String?
        if (System.getProperty("java.version").startsWith("1.7")) {
            permName = "PermGen space"
            lblPerm = msg?.getMessage("label.permgen.space", null, locale)
        } else {
            permName = "Metaspace"
            lblPerm = msg?.getMessage("label.metaspace", null, locale)
        }
        mav.addObject("permname", permName)
        mav.addObject("memoryleak2func", msg?.getMessage("function.name.memory.leak2", arrayOf<Any>(lblPerm!!), locale))
        mav.addObject("memoryleak2desc", msg?.getMessage("function.description.memory.leak2", arrayOf<Any>(lblPerm!!), locale))

        val mode = System.getProperty("easybuggy.mode")
        mav.addObject("isOnlyVulnerabilities", mode != null && mode.equals("only-vulnerabilities", ignoreCase = true))
        return mav
    }
}