package org.t246osslab.easybuggy4kt.exceptions

import java.util.ResourceBundle

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class MissingResourceExceptionController {

    @RequestMapping(value = "/mre")
    fun process() {
        ResourceBundle.getBundle("")
    }
}