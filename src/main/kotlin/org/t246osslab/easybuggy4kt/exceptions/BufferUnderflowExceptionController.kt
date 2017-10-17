package org.t246osslab.easybuggy4kt.exceptions

import java.nio.ByteBuffer

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class BufferUnderflowExceptionController {

    @RequestMapping(value = "/bue")
    fun process() {
        ByteBuffer.wrap(byteArrayOf(1)).double
    }
}