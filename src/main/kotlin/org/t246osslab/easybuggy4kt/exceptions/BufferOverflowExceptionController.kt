package org.t246osslab.easybuggy4kt.exceptions

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

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