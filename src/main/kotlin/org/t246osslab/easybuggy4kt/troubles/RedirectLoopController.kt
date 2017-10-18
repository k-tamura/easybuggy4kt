package org.t246osslab.easybuggy4kt.troubles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException

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