package org.t246osslab.easybuggy4kt.errors

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.net.NetworkInterface
import java.net.SocketException

@Controller
class UnsatisfiedLinkErrorController {

    @Throws(SocketException::class)
    private external fun getByName0(name: String): NetworkInterface

    @RequestMapping(value = "/jnicall")
    @Throws(SocketException::class)
    fun process() {
        getByName0("")
    }
}
