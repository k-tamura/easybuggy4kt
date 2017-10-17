package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class StackOverflowErrorController {

    @RequestMapping(value = "/sofe")
    fun process() {
        S().toString()
    }

    inner class S {
        override fun toString(): String {
            return "" + this
        }
    }
}
