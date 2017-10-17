package org.t246osslab.easybuggy4kt.exceptions

import java.util.Scanner

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class InputMismatchExceptionController {

    @RequestMapping(value = "/ime")
    fun process() {
        Scanner("a").use { scanner -> scanner.nextInt() }
    }
}
