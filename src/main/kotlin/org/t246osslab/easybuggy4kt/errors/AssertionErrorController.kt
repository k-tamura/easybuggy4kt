package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class AssertionErrorController {

    @RequestMapping(value = "/asserr")
    fun process() {
        assert(false) { "Invalid!" }
    }
}
