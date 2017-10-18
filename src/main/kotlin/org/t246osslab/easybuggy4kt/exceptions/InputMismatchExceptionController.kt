package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class InputMismatchExceptionController {

    @RequestMapping(value = "/ime")
    fun process() {
        Scanner("a").use { scanner -> scanner.nextInt() }
    }
}
