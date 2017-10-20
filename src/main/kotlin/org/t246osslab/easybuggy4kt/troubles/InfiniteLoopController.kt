package org.t246osslab.easybuggy4kt.troubles

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

@Controller
class InfiniteLoopController {

    @RequestMapping(value = "/infiniteloop")
    fun process(req: HttpServletRequest) {
        while (true) {
            val contextPath = req.contextPath
            val contentLength = req.contentLength
            log.debug("contextPath: {}, contentLength: {}", contextPath, contentLength)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(InfiniteLoopController::class.java)
    }
}