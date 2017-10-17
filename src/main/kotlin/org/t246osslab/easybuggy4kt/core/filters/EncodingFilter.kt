package org.t246osslab.easybuggy4kt.core.filters

import java.io.IOException

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.boot.web.filter.OrderedCharacterEncodingFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component

/**
 * Servlet Filter for encoding
 */
@Component
class EncodingFilter : OrderedCharacterEncodingFilter() {

    /**
     * Set the encoding to use for requests.
     * "Shift_JIS" is intentionally set to the request to /mojibake.
     */
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        super.setOrder(Ordered.HIGHEST_PRECEDENCE)
        if ("/mojibake" == request.requestURI) {
            super.setEncoding("Shift_JIS")
        } else {
            super.setEncoding("UTF-8")
        }
        super.setForceEncoding(true)
        super.doFilterInternal(request, response, filterChain)
    }
}
