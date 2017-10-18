package org.t246osslab.easybuggy4kt.troubles

import javassist.CannotCompileException
import javassist.ClassPool
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.lang.management.ManagementFactory
import java.lang.management.MemoryPoolMXBean
import java.lang.management.MemoryType
import java.util.*

@Controller
class MemoryLeakController2 : AbstractController() {

    private var i = 0

    @RequestMapping(value = "/memoryleak2")
    fun process(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "memoryleak")
        mav.addObject("title", msg?.getMessage("title.memoryleak2.page", null, locale))
        val permName = if (System.getProperty("java.version").startsWith("1.7"))
            msg?.getMessage("label.permgen.space", null, locale)
        else
            msg?.getMessage("label.metaspace", null, locale)
        mav.addObject("note", msg?.getMessage("msg.permgen.space.leak.occur", arrayOf<String?>(permName), locale))
        try {
            toDoRemove()

            val nonHeapPoolMXBeans = ArrayList<MemoryPoolMXBean>()
            val memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans()
            for (memoryPoolMXBean in memoryPoolMXBeans) {
                if (MemoryType.NON_HEAP == memoryPoolMXBean.type) {
                    nonHeapPoolMXBeans.add(memoryPoolMXBean)
                }
            }
            mav.addObject("memoryPoolMXBeans", nonHeapPoolMXBeans)

        } catch (e: Exception) {
            log.error("Exception occurs: ", e)
            mav.addObject("errmsg",
                    msg?.getMessage("msg.unknown.exception.occur", arrayOf<String?>(e.message), null, locale))

        }

        return mav
    }

    @Throws(CannotCompileException::class)
    private fun toDoRemove() {
        val j = i + 1000
        val pool = ClassPool.getDefault()
        while (i < j) {
            pool.makeClass("org.t246osslab.easybuggy.core.model.TestClass" + i).toClass()
            i++
        }
    }
}
