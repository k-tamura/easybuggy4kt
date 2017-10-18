package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.xml.parsers.SAXParserFactory

@Controller
class FactoryConfigurationErrorController {

    @RequestMapping(value = "/fce")
    fun process() {
        System.setProperty("javax.xml.parsers.SAXParserFactory", "non-exist-factory")
        SAXParserFactory.newInstance()
    }
}
