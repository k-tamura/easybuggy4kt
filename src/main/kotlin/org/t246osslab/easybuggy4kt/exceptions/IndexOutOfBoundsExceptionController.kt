package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class IndexOutOfBoundsExceptionController {

    @RequestMapping("/ioobe")
    fun process() {
        ArrayList<String>()[1]
    }
}