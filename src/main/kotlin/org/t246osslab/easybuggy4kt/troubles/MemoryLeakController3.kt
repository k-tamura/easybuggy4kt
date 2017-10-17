package org.t246osslab.easybuggy4kt.troubles

import java.util.Locale
import java.util.TimeZone
import java.util.zip.Deflater

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.t246osslab.easybuggy4kt.controller.AbstractController

@Controller
class MemoryLeakController3 : AbstractController() {

    @RequestMapping(value = "/memoryleak3")
    fun process(mav: ModelAndView, locale: Locale): ModelAndView {
        setViewAndCommonObjects(mav, locale, "memoryleak3")
        mav.addObject("timeZone", TimeZone.getDefault())
        toDoRemove()
        return mav
    }

    private fun toDoRemove() {
        val inputString = "inputString"
        val input = inputString.toByteArray()
        val output = ByteArray(100)
        for (i in 0..999) {
            val compresser = Deflater()
            compresser.setInput(input)
            compresser.deflate(output)
        }
    }
}
