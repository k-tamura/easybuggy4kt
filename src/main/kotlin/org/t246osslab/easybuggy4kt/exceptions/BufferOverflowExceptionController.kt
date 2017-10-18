package org.t246osslab.easybuggy4kt.exceptions

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel.MapMode

@Controller
class BufferOverflowExceptionController {

    @RequestMapping(value = "/boe")
    fun process() {
        val f = File("test.txt")
        try {
            RandomAccessFile(f, "rw").use { raf ->
                val ch = raf.channel
                val buf = ch.map(MapMode.READ_WRITE, 0, f.length())
                val src = ByteArray(10)
                buf.put(src)
            }
        } catch (e: FileNotFoundException) {
            log.error("FileNotFoundException occurs: ", e)
        } catch (e: IOException) {
            log.error("IOException occurs: ", e)
        }

    }

    companion object {

        private val log = LoggerFactory.getLogger(BufferOverflowExceptionController::class.java)
    }
}