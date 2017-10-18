package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class OutOfMemoryErrorController4 {

    @RequestMapping(value = "/oome4")
    fun process() {
        val properties = System.getProperties()
        val r = Random()
        while (true) {
            properties.put(r.nextInt(), "value")
        }
    }
}
