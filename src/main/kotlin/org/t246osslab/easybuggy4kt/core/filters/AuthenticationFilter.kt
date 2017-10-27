package org.t246osslab.easybuggy4kt.core.filters

import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * Servlet Filter for authentication
 */
@Component
class AuthenticationFilter : Filter {

    /**
     * Intercept unauthenticated requests for specific URLs and redirect to login page.
     *
     * @see Filter.doFilter
     */
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        val target = request.requestURI

        if (target.startsWith("/admins") || "/uid/serverinfo.jsp" == target || "/serverinfo" == target) {
            /* Login (authentication) is needed to access admin pages (under /admins). */

            val loginType = request.getParameter("logintype")
            var queryString: String? = request.queryString
            if (queryString == null) {
                queryString = ""
            } else {
                /* Remove "logintype" parameter from query string.
                    (* "logintype" specifies a login servlet) */
                queryString = queryString.replace("logintype=$loginType&", "")
                queryString = queryString.replace("&logintype=" + loginType!!, "")
                queryString = queryString.replace("logintype=" + loginType, "")
                if (queryString.isNotEmpty()) {
                    queryString = "?" + queryString
                }
            }
            var session: HttpSession? = request.getSession(false)
            if (session == null || session.getAttribute("authNMsg") == null
                    || "authenticated" != session.getAttribute("authNMsg")) {
                /* Not authenticated yet */
                session = request.getSession(true)
                session!!.setAttribute("target", target)
                when (loginType) {
                    null -> response.sendRedirect(response.encodeRedirectURL("/login" + queryString))
                    "sessionfixation" -> response.sendRedirect(response.encodeRedirectURL("/$loginType/login$queryString"))
                    else -> response.sendRedirect("/$loginType/login$queryString")
                }
                return
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
}// Do nothing
