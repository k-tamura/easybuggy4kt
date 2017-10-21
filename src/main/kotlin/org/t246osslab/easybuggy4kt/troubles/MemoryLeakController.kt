package org.t246osslab.easybuggy4kt.troubles

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController
import java.lang.management.ManagementFactory
import java.lang.management.MemoryType
import java.util.*

@Controller
class MemoryLeakController : AbstractController() {

    private val cache = HashMap<String, String>()

    @RequestMapping(value = "/memoryleak")
    fun process(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "memoryleak")
        toDoRemove()

        val memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans()
        val heapPoolMXBeans = memoryPoolMXBeans.filter { MemoryType.HEAP == it.type }
        mav.addObject("memoryPoolMXBeans", heapPoolMXBeans)
        return mav
    }

    private fun toDoRemove() {
        val sb = StringBuilder()
        for (i in 0..99999) {
            sb.append("Memory leak occurs!")
        }
        cache.put(sb.hashCode().toString(), sb.toString())
    }
}
