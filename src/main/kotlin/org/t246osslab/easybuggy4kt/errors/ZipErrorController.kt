package org.t246osslab.easybuggy4kt.errors

import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.FileSystems
import java.util.HashMap

import javax.servlet.http.HttpServletRequest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ZipErrorController {

    @RequestMapping(value = "/ze")
    fun process(req: HttpServletRequest) {
        try {
            val file = File(req.servletContext.getAttribute("javax.servlet.context.tempdir").toString(),
                    "test.zip")
            val uri = URI.create("jar:file:" + file.absolutePath)
            file.createNewFile()
            FileSystems.newFileSystem(uri, HashMap<String, Any>())
        } catch (e: IOException) {
        }

    }
}
