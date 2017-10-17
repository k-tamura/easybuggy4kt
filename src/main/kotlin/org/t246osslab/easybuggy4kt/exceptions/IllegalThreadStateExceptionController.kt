package org.t246osslab.easybuggy4kt.exceptions

import java.io.IOException

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

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
