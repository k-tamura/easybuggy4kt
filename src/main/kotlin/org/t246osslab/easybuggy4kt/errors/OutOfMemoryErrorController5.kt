package org.t246osslab.easybuggy4kt.errors

import javassist.ClassPool
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class OutOfMemoryErrorController5 {

    @RequestMapping(value = "/oome5")
    fun process() {
        try {
            for (i in 0..999999) {
                val pool = ClassPool.getDefault()
                pool.makeClass("org.t246osslab.easybuggy4kt.Generated" + i).toClass()
            }
        } catch (e: Exception) {
        }

    }
}
