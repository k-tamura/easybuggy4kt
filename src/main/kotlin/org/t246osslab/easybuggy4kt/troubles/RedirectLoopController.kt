package org.t246osslab.easybuggy4kt.troubles

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException

@Controller
class RedirectLoopController {

    @RequestMapping(value = "/redirectloop")
    @Throws(IOException::class)
    fun process(): String {
        return "redirect:redirectloop"
    }
}