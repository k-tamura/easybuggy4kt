package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class OutOfMemoryErrorController {

    @RequestMapping(value = "/oome")
    fun process() {
        val sb = StringBuilder()
        while (true) {
            sb.append("OutOfMemoryError!")
        }
    }
}
