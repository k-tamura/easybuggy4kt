package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.util.*

@Controller
class ConcurrentModificationExceptionController {

    @RequestMapping(value = "/cme")
    fun process() {
        val list = ArrayList<String>()
        list.add("1")
        list.add("2")

        val iter = list.iterator()
        while (iter.hasNext()) {
            val s = iter.next()
            if ("2" == s) {
                list.remove(s)
            }
        }
    }
}