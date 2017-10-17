package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class OutOfMemoryErrorController3 {

    @RequestMapping(value = "/oome3")
    fun process() {
        while (true) {
            object : Thread() {
                override fun run() {
                    try {
                        Thread.sleep(10000)
                    } catch (e: InterruptedException) {
                    }

                }
            }.start()
        }
    }
}
