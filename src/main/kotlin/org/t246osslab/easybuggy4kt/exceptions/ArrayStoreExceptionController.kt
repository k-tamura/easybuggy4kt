package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ArrayStoreExceptionController {

    @RequestMapping(value = "/ase")
    fun process() {
        val objects = arrayOfNulls<String>(1)
        objects[0] = Integer.valueOf(1).toString()
    }
}