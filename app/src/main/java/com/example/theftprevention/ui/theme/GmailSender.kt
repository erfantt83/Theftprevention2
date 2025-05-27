package com.example.theftprevention

import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GmailSender {
    private val user = "antitheftmontazere@gmail.com" // ایمیلی که ساختی برای فرستادن
    private val password = "eeoxqnrlgpmbogli" // app password مخصوص این ایمیل

    fun sendMail(subject: String, body: String, sender: String, recipients: String) {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.port"] = "587"

        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(user, password)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(sender))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients))
            message.subject = subject
            message.setText(body)
            Transport.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}