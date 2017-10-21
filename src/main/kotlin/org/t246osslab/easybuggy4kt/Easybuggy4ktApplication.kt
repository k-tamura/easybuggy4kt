package org.t246osslab.easybuggy4kt

import org.apache.catalina.servlets.DefaultServlet
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Easybuggy4ktApplication : SpringBootServletInitializer() {

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(Easybuggy4ktApplication::class.java)
    }

    @Bean
    fun servletRegistrationBean(): ServletRegistrationBean {
        /* Enable directory listing under /uid/ */
        val servlet = DefaultServlet()
        val bean = ServletRegistrationBean(servlet, "/uid/*")
        bean.isEnabled = true
        bean.addInitParameter("listings", "true")
        bean.setLoadOnStartup(1)
        return bean
    }

    @Bean
    fun executorListener(): InitializationListener {
        return InitializationListener()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(arrayOf(Easybuggy4ktApplication::class.java, InitializationListener::class.java), args)
        }
    }
}
