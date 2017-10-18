package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException

@Controller
class IllegalThreadStateExceptionController {

    @RequestMapping(value = "/itse")
    fun process() {
        val rt = Runtime.getRuntime()
        try {
            val proc = rt.exec("javac")
            proc.exitValue()
        } catch (e: IOException) {
        }

    }
}
