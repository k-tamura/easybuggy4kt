package org.t246osslab.easybuggy4kt.exceptions

import java.io.IOException
import java.net.URI
import java.nio.file.FileSystems
import java.util.HashMap

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class FileSystemNotFoundExceptionController {

    @RequestMapping(value = "/fsnfe")
    fun process() {
        val uri = URI.create("jar:file:/not/exist.zip")
        try {
            FileSystems.newFileSystem(uri, HashMap<String, Any>())
        } catch (e: IOException) {
        }

    }
}