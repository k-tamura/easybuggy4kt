package org.t246osslab.easybuggy4kt.exceptions

import java.util.ArrayList

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class NoSuchElementExceptionController {

    @RequestMapping(value = "/nsee")
    fun process() {
        ArrayList<String>().iterator().next()
    }
}
