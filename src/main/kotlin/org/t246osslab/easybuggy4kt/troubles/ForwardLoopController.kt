package org.t246osslab.easybuggy4kt.troubles

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException
import javax.servlet.ServletException

@Controller
class ForwardLoopController {

    @RequestMapping(value = "/forwardloop")
    @Throws(IOException::class, ServletException::class)
    fun process(): String {
        return "forward:forwardloop"
    }
}