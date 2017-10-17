package org.t246osslab.easybuggy4kt.core.filters

import java.io.IOException

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component

/**
 * Servlet Filter for security
 */
/**
 * Default constructor.
 */
@Component
class SecurityFilter : Filter {

    @Autowired
    internal var msg: MessageSource? = null

    /**
     * Prevent several security vulnerabilities.
     *
     * @see Filter.doFilter
     */
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        val target = request.requestURI

        /* Prevent clickjacking if target is not /admins/clickjacking ... */
        if (!target.startsWith("/admins/clickjacking")) {
            response.addHeader("X-FRAME-OPTIONS", "DENY")
        }
        /* Prevent Content-Type sniffing */
        response.addHeader("X-Content-Type-Options", "nosniff")

        /* Prevent XSS if target is not /xss ... */
        if (!target.startsWith("/xss")) {
            response.addHeader("X-XSS-Protection", "1; mode=block")
        }

        /* Prevent to upload large files if target start w/ /ureupload and /xee and /xxe */
        if ((target.startsWith("/ureupload") || target.startsWith("/xee") || target.startsWith("/xxe")) && request.method.equals("POST", ignoreCase = true)) {
            val upload = ServletFileUpload()
            upload.fileItemFactory = DiskFileItemFactory()
            upload.fileSizeMax = FILE_SIZE_MAX.toLong() // 10MB
            upload.sizeMax = REQUEST_SIZE_MAX.toLong() // 50MB
            try {
                upload.parseRequest(ServletRequestContext(request))
            } catch (e: FileUploadException) {
                req.setAttribute("errorMessage", msg!!.getMessage("msg.max.file.size.exceed", null, request.locale))
            }

        }

        chain.doFilter(req, res)
    }

    override fun destroy() {
        // Do nothing
    }

    @Throws(ServletException::class)
    override fun init(arg0: FilterConfig) {
        // Do nothing
    }

    companion object {

        /**
         * The maximum size permitted for the complete request.
         */
        private val REQUEST_SIZE_MAX = 1024 * 1024 * 50

        /**
         * The maximum size permitted for a single uploaded file.
         */
        private val FILE_SIZE_MAX = 1024 * 1024 * 10
    }
}// Do nothing
