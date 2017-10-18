package org.t246osslab.easybuggy4kt

import org.owasp.esapi.ESAPI
import java.io.OutputStream
import java.io.PrintStream
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class InitializationListener : ServletContextListener {
    override fun contextInitialized(event: ServletContextEvent) {

        /*
         * Suppress noisy messages output by the ESAPI library. For more detail:
         * https://stackoverflow.com/questions/45857064/how-to-suppress-messages-output-by-esapi-library
         */
        val original = System.out
        try {
            PrintStream(object : OutputStream() {
                override fun write(b: Int) {
                    // Do nothing
                }
            }).use { out ->
                System.setOut(out)
                System.setErr(out)
                ESAPI.encoder()
            }
        } catch (e: Exception) {
            // Do nothing
        } finally {
            System.setOut(original)
        }
    }

    override fun contextDestroyed(sce: ServletContextEvent) {
        // Do nothing
    }
}
