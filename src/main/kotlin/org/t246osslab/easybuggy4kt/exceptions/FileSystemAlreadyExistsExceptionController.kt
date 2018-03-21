package org.t246osslab.easybuggy4kt.exceptions

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.File
import java.io.IOException
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class FileSystemAlreadyExistsExceptionController {

    @RequestMapping(value = "/fsaee")
    fun process(req: HttpServletRequest) {

        val tmpDir = req.servletContext.getAttribute("javax.servlet.context.tempdir").toString()
        val zipfile = URI.create("jar:file:" + tmpDir + File.separator + "fsaee.zip")
        val env = HashMap<String, String>()
        env["create"] = "true"
        env["encoding"] = "UTF-8"
        try {
            FileSystems.newFileSystem(zipfile, env).use { zipfs ->
                Files.write(zipfs.getPath("fsaee.txt"), "test".toByteArray(charset("UTF-8")), StandardOpenOption.CREATE)
                FileSystems.newFileSystem(zipfile, env)
                FileSystems.newFileSystem(zipfile, env)
            }
        } catch (e1: IOException) {
        }

    }
}