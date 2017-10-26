package org.t246osslab.easybuggy4kt.core.utils

import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Utility class to handle multi part files.
 */
class MultiPartFileUtils

// squid:S1118: Utility classes should not have public constructors
private constructor() {

    private val log = LoggerFactory.getLogger(MultiPartFileUtils::class.java)

    init {
        throw IllegalAccessError("Utility class")
    }

    companion object {

        private val log = LoggerFactory.getLogger(MultiPartFileUtils::class.java)

        /**
         * Write uploaded file to the given path.
         *
         * @param savePath Path to save an uploaded file.
         * @param filePart A part or form item that was received within a <code>multipart/form-data</code> POST request.
         * @param fileName The uploaded file name.
         */
        @Throws(IOException::class)
        fun writeFile(savePath: String, filePart: MultipartFile, fileName: String): Boolean {
            var isRegistered = false
            try {
                FileOutputStream(savePath + File.separator + fileName).use { out ->
                    filePart.inputStream.use { `in` ->
                        val bytes = ByteArray(1024)
                        var read = `in`.read(bytes)
                        while (read != -1) {
                            out.write(bytes, 0, read)
                            read = `in`.read(bytes)
                        }
                    }
                }
            } catch (e: FileNotFoundException) {
                // Ignore because file already exists
                log.debug("Exception occurs: ", e)
                isRegistered = true
            }

            return isRegistered
        }
    }
}
