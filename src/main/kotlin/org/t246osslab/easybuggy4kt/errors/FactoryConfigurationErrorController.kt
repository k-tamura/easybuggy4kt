package org.t246osslab.easybuggy4kt.errors

import javax.xml.parsers.SAXParserFactory

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class FactoryConfigurationErrorController {

    @RequestMapping(value = "/fce")
    fun process() {
        System.setProperty("javax.xml.parsers.SAXParserFactory", "non-exist-factory")
        SAXParserFactory.newInstance()
    }
}
