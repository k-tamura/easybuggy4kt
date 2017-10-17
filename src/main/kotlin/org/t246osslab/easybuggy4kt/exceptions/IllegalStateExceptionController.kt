package org.t246osslab.easybuggy4kt.exceptions

import java.util.ArrayList
import java.util.Arrays

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IllegalStateExceptionController {

    @RequestMapping(value = "/iase")
    fun process() {
        val alphabet = ArrayList(Arrays.asList("a", "b, c"))
        val itr = alphabet.iterator()
        while (itr.hasNext()) {
            itr.remove()
        }
    }
}
