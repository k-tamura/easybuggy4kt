package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class UnsupportedOperationExceptionController {

    @RequestMapping(value = "/uoe")
    fun process() {
        val alphabet = Arrays.asList("a", "b", "c")
        val i = alphabet.iterator()
        while (i.hasNext()) {
            val name = i.next()
            if ("a" != name) {
                i.remove()
            }
        }
    }
}
