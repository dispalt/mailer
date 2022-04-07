package com.github.jurajburian.mailer

import jakarta.mail.Session
import jakarta.mail.internet.{MimeMessage => JavaMimeMessage}

class MimeMessage(session: Session) extends JavaMimeMessage(session) {

  override def updateMessageID(): Unit = {
    Option(headers.getHeader("Message-ID", null)) match {
      case Some(_) => ()
      case None    => super.updateMessageID()
    }
  }

}