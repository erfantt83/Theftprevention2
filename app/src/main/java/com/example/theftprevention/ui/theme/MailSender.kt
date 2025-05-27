package com.example.theftprevention

import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.concurrent.thread

object MailSender {
    private const val EMAIL = "antitheftmontazere@gmail.com"
    private const val PASSWORD = "eeoxqnrlgpmbogli" // App password

    fun sendEmail(recipient: String, subject: String, body: String) {
        thread {
            try {
                val props = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                }

                val session = Session.getInstance(props, object : javax.mail.Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(EMAIL, PASSWORD)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(EMAIL))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                    setSubject(subject)
                    setText(body)
                }

                Transport.send(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}