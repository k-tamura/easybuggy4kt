package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage

@Controller
class ImagingOpExceptionController {

    @RequestMapping(value = "/imoe")
    fun process() {
        val img = BufferedImage(1, 40000, BufferedImage.TYPE_INT_RGB)
        val flipAtop = AffineTransformOp(AffineTransform.getScaleInstance(1.0, 1.0),
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
        flipAtop.filter(img, null)
    }
}
