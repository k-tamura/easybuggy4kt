package org.t246osslab.easybuggy4kt.errors

import javax.xml.transform.TransformerFactory

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class TransformerFactoryConfigurationErrorController {

    @RequestMapping(value = "/tfce")
    fun process() {
        System.setProperty("javax.xml.transform.TransformerFactory", "a")
        TransformerFactory.newInstance()
    }
}
