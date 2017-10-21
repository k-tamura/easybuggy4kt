package org.t246osslab.easybuggy4kt.exceptions

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class EmptyStackExceptionController {

    @RequestMapping(value = "/ese")
    fun process() {
        val stack = Stack<String>()
        var tmp: String? = stack.pop()
        while (null != tmp) {
            tmp = stack.pop()
            log.debug("Stack.pop(): {}", tmp)
        }
    }

    companion object {

        private val log = LoggerFactory.getLogger(EmptyStackExceptionController::class.java)
    }
}