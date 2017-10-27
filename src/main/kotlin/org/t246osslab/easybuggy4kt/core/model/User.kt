package org.t246osslab.easybuggy4kt.core.model

import org.springframework.ldap.odm.annotations.Attribute
import org.springframework.ldap.odm.annotations.Entry
import org.springframework.ldap.odm.annotations.Id
import java.io.Serializable
import java.util.*
import javax.naming.Name

@Entry(objectClasses = arrayOf("person", "inetOrgPerson"))
class User : Serializable {
    @Id
    var dn: Name? = null
    @Attribute(name = "uid")
    var userId: String? = null
    var name: String? = null
    @Attribute(name = "userPassword")
    var password: String? = null
    var secret: String? = null
    var phone: String? = null
    var mail: String? = null
    var loginFailedCount = 0L
    var lastLoginFailedTime: Date? = null

    override fun toString(): String {
        return "User dn=$dn [userId=$userId, name=$name, password=$password, secret=$secret, phone=$phone, " +
                "mail=$mail, loginFailedCount=$loginFailedCount, lastLoginFailedTime=$lastLoginFailedTime]"
    }

    companion object {

        private const val serialVersionUID = 1L
    }
}
