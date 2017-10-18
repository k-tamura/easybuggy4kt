package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.xml.transform.TransformerFactory

@Controller
class TransformerFactoryConfigurationErrorController {

    @RequestMapping(value = "/tfce")
    fun process() {
        System.setProperty("javax.xml.transform.TransformerFactory", "a")
        TransformerFactory.newInstance()
    }
}
