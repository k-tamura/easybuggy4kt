package org.t246osslab.easybuggy4kt.troubles

import java.io.IOException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class RedirectLoopController {

    @Autowired
    internal var msg: MessageSource? = null

    @RequestMapping(value = "/redirectloop")
    @Throws(IOException::class)
    fun process(): String {
        return "redirect:redirectloop"
    }
}