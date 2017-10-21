package org.t246osslab.easybuggy4kt.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
abstract class AbstractController {

    protected var log = LoggerFactory.getLogger(this.javaClass)!!

    @Autowired
    protected var msg: MessageSource? = null

    protected fun setViewAndCommonObjects(mav: ModelAndView, locale: Locale, viewName: String?) {
        if (viewName != null) {
            mav.viewName = viewName
            try {
                mav.addObject("title", msg!!.getMessage("title.$viewName.page", null, locale))
            } catch (e: NoSuchMessageException) {
                log.debug("'title.$viewName.page' is not in messages.properties", e)
                mav.addObject("title", "title.$viewName.page")
            }

            try {
                mav.addObject("note", msg!!.getMessage("msg.note." + viewName, null, locale))
            } catch (e: NoSuchMessageException) {
                log.debug("'msg.note.$viewName' is not in messages.properties", e)
            }

        } else {
            log.warn("viewName is null")
        }
    }
}
