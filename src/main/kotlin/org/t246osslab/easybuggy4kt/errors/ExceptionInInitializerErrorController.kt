package org.t246osslab.easybuggy4kt.errors

import java.lang.reflect.Constructor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ExceptionInInitializerErrorController {

    @RequestMapping(value = "/eie")
    fun process() {
        try {
            val cl = Class.forName("org.t246osslab.easybuggy4kt.errors.InitializerErrorThrower")
            val cunstructor = cl.getConstructor()
            cunstructor.newInstance(*arrayOf<Any>())
        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
        }

    }

    companion object {

        private val log = LoggerFactory.getLogger(ExceptionInInitializerErrorController::class.java)
    }
}

internal object InitializerErrorThrower {
    init {
        LoggerFactory.getLogger(InitializerErrorThrower::class.java).debug("clinit" + 1 / 0)
    }
}// this constructor is added to suppress sonar advice