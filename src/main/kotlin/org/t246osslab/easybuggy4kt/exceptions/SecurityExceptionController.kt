package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class SecurityExceptionController {

    @RequestMapping(value = "/se")
    fun process() {
        SecurityManager().checkPermission(RuntimePermission("exitVM"), null)
    }
}
