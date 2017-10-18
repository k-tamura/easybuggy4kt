package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.awt.geom.GeneralPath

@Controller
class IllegalPathStateExceptionController {

    @RequestMapping(value = "/ipse")
    fun process() {
        val subPath = GeneralPath(GeneralPath.WIND_EVEN_ODD, 100)
        subPath.closePath()
    }
}
