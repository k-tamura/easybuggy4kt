package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class MissingResourceExceptionController {

    @RequestMapping(value = "/mre")
    fun process() {
        ResourceBundle.getBundle("")
    }
}