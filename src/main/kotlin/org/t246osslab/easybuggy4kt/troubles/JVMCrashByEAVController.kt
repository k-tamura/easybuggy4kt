package org.t246osslab.easybuggy4kt.troubles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import sun.misc.Unsafe
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class JVMCrashByEAVController {

    @Autowired
    internal var msg: MessageSource? = null

    private val unsafe: Unsafe
        @Throws(NoSuchFieldException::class, IllegalAccessException::class)
        get() {
            val singleoneInstanceField = Unsafe::class.java.getDeclaredField("theUnsafe")
            singleoneInstanceField.isAccessible = true
            return singleoneInstanceField.get(null) as Unsafe
        }

    @RequestMapping(value = "/jvmcrasheav")
    fun process(req: HttpServletRequest, res: HttpServletResponse) {
        try {
            unsafe.getByte(0)
        } catch (e: Exception) {
        }

    }
}