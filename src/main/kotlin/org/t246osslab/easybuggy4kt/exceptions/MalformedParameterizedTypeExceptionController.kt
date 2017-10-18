package org.t246osslab.easybuggy4kt.exceptions

import java.lang.reflect.Type

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl

@Controller
class MalformedParameterizedTypeExceptionController {

    @RequestMapping(value = "/mpte")
    fun process() {
        ParameterizedTypeImpl.make(List::class.java, arrayOf(), null)
    }
}