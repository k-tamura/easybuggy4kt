package org.t246osslab.easybuggy4kt.exceptions

import java.awt.geom.GeneralPath

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class IllegalPathStateExceptionController {

    @RequestMapping(value = "/ipse")
    fun process() {
        val subPath = GeneralPath(GeneralPath.WIND_EVEN_ODD, 100)
        subPath.closePath()
    }
}
