package org.t246osslab.easybuggy4kt.troubles

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import sun.misc.Unsafe

@Controller
class JVMCrashByEAVController {

    private val unsafe: Unsafe
        @Throws(NoSuchFieldException::class, IllegalAccessException::class)
        get() {
            val singleoneInstanceField = Unsafe::class.java.getDeclaredField("theUnsafe")
            singleoneInstanceField.isAccessible = true
            return singleoneInstanceField.get(null) as Unsafe
        }

    @RequestMapping(value = "/jvmcrasheav")
    fun process() {
        try {
            unsafe.getByte(0)
        } catch (e: Exception) {
        }

    }
}