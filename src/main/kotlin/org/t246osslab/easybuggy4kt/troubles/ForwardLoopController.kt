package org.t246osslab.easybuggy4kt.troubles

import java.io.IOException

import javax.servlet.ServletException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ForwardLoopController {

    @Autowired
    internal var msg: MessageSource? = null

    @RequestMapping(value = "/forwardloop")
    @Throws(IOException::class, ServletException::class)
    fun process(): String {
        return "forward:forwardloop"
    }
}