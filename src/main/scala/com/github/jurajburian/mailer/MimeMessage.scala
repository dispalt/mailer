package com.github.jurajburian.mailer

import java.util

import javax.mail.Session
import javax.mail.internet.{MimeMessage => JavaMimeMessage}

class MimeMessage(session: Session) extends JavaMimeMessage(session) {

  override def getAllHeaders: util.Enumeration[_] = super.getAllHeaders

  override def updateMessageID(): Unit = {
    Option(headers.getHeader("Message-ID", null)) match {
      case Some(_) => ()
      case None    => super.updateMessageID()
    }
  }

}
