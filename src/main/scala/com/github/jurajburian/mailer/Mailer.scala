package com.github.jurajburian.mailer

import java.io.File

import jakarta.activation.{DataHandler, FileDataSource}
import jakarta.mail.internet.{InternetAddress, MimeBodyPart, MimeMultipart, PreencodedMimeBodyPart}
import jakarta.mail.util.ByteArrayDataSource
import jakarta.mail.{MessagingException, Session, Transport}


/**
	* Represents the content of the e-mail message, composed of individual `MimeBodyPart` instances.
	* For easier use, helper methods to add specific content are available, such as `html()` for
	* adding ''HTML'' or `attachFile()` to add file attachment.
	*
	* @param parts parts of the message content (represented by `MimeBodyPart` instances)
	*/
case class Content(parts: MimeBodyPart*) {

	/**
		* Appends the given part (represented by `MimeBodyPart` instance) to the existing content parts.
		*
		* @param parts content part to append
		* @return instance of the [[com.github.jurajburian.mailer.Content]] class with appended
		*         content part
		*/
	def append(parts: MimeBodyPart*) = Content(this.parts ++ parts: _*)

	/**
		* Appends the given string as the text content part (defaults to ''text/plain'').
		*
		* @param text    text to append
		* @param charset charset of the given text (defaults to ''UTF-8'')
		* @param subtype defines subtype of the ''MIME type'' (the part after the slash), defaults
		*                to ''UTF-8''
		* @param headers content part headers (''RFC 822'')
		* @return instance of the [[com.github.jurajburian.mailer.Content]] class with appended
		*         content part
		*/
	def text(text: String, charset: String = "UTF-8", subtype: String = "plain",
					 headers: Seq[MessageHeader] = Seq.empty[MessageHeader]): Content = {

		val part = new MimeBodyPart()
		part.setText(text, charset, subtype)
		headers.foreach(header => part.setHeader(header.name, header.value))
		append(part)
	}

	/**
		* Appends the given ''HTML'' string as the new ''HTML'' content part.
		*
		* @param html    ''HTML'' string to append
		* @param charset charset of the given ''HTML'' string (defaults to ''UTF-8'')
		* @param headers content part headers (''RFC 822'')
		* @return instance of the [[com.github.jurajburian.mailer.Content]] class with appended
		*         content part
		*/
	def html(html: String, charset: String = "UTF-8",
					 headers: Seq[MessageHeader] = Seq.empty[MessageHeader]): Content = {

		val part = new MimeBodyPart()
		part.setText(html, charset, "html")
		headers.foreach(header => part.setHeader(header.name, header.value))
		append(part)
	}

	/**
		* Appends the given file as the e-mail message attachment.
		*
		* @param file      file to attach
		* @param name      name of the attachment (optional, defaults to the given file name)
		* @param contentId the "Content-ID" header field of this body part
		* @param headers   content part headers (''RFC 822'')
		* @return instance of the [[com.github.jurajburian.mailer.Content]] class with appended
		*         content part
		*/

	def attachFile(file: File, name: Option[String] = None,
								 contentId: Option[String] = None,
								 headers: Seq[MessageHeader] = Seq.empty[MessageHeader]): Content = {

		val part = new MimeBodyPart()
		contentId.foreach(part.setContentID)
		part.setDataHandler(new DataHandler(new FileDataSource(file)))
		part.setFileName(name.getOrElse(file.getName))
		headers.foreach(header => part.setHeader(header.name, header.value))
		append(part)
	}

	/**
		* Appends the given array of bytes as the e-mail message attachment. Useful especially when the
		* original file object is not available, only its array of bytes.
		*
		* @param bytes     array of bytes representing the attachment
		* @param mimeType  ''MIME type'' of the attachment
		* @param name      name of the attachment (optional)
		* @param contentId the "Content-ID" header field of this body part (optional)
		* @param headers   content part headers (''RFC 822'')
		* @return instance of the [[com.github.jurajburian.mailer.Content]] class with appended
		*         content part
		*/
	def attachBytes(bytes: Array[Byte], mimeType: String, name: Option[String] = None,
									contentId: Option[String] = None,
									headers: Seq[MessageHeader] = Seq.empty[MessageHeader]): Content = {

		val part = new MimeBodyPart()
		contentId.foreach(part.setContentID)
		part.setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, mimeType)))
		name.foreach(part.setFileName)
		headers.foreach(header => part.setHeader(header.name, header.value))
		append(part)
	}


	/**
		* Appends the given string of ''Base64-encoded'' data as the e-mail message attachment. Use
		* instead of the `#attachBytes` method if you have already ''Base64-encoded'' data and you
		* want to avoid ''JavaMail'' encoding it again.
		*
		* @param data      ''Base64-encoded'' data
		* @param mimeType  ''MIME type'' of the attachment
		* @param name      name of the attachment (optional)
		* @param contentId the `Content-ID` header field of this body part (optional)
		* @param headers   content part headers (''RFC 822'')
		* @return instance of the [[com.github.jurajburian.mailer.Content]] class with appended
		*         content part
		* @see http://www.oracle.com/technetwork/java/faq-135477.html#preencoded
		*/
	def attachBase64(data: String, mimeType: String, name: Option[String] = None,
									 contentId: Option[String] = None,
									 headers: Seq[MessageHeader] = Seq.empty[MessageHeader]): Content = {

		val part = new PreencodedMimeBodyPart("base64")
		contentId.foreach(part.setContentID)
		part.setDataHandler(new DataHandler(new ByteArrayDataSource(data, mimeType)))
		name.foreach(part.setFileName)
		headers.foreach(header => part.setHeader(header.name, header.value))
		append(part)
	}


	@throws[MessagingException]
	def apply() = {
		val prts = parts
		new MimeMultipart() {
			prts.foreach(addBodyPart(_))
		}
	}
}

/**
	* Represents the e-mail message itself.
	*
	* @param from       e-mail sender address
	* @param subject    e-mail subject text
	* @param content    e-mail content, represented by the instance of
	*                   [[com.github.jurajburian.mailer.Content]] class
	* @param to         set of e-mail receiver addresses
	* @param cc         set of e-mail ''carbon copy'' receiver addresses
	* @param bcc        set of e-mail ''blind carbon copy'' receiver addresses
	* @param replyTo    address used to reply this message (optional)
	* @param replyToAll whether the new message will be addressed to all recipients of this message
	* @param headers    message headers (''RFC 822'')
	*/
case class Message(from: InternetAddress,
									 subject: String,
									 content: Content,
									 to: Seq[InternetAddress] = Seq.empty[InternetAddress],
									 cc: Seq[InternetAddress] = Seq.empty[InternetAddress],
									 bcc: Seq[InternetAddress] = Seq.empty[InternetAddress],
									 replyTo: Option[InternetAddress] = None,
									 replyToAll: Option[Boolean] = None,
									 headers: Seq[MessageHeader] = Seq.empty[MessageHeader]) {

}

/**
	* Represents the ''Mailer'' itself, with methods for opening/closing the connection and sending
	* the message ([[com.github.jurajburian.mailer.Message]])
	*/
trait Mailer {

	/**
		* Creates new transport connection.
		*
		* @return instance of the [[com.github.jurajburian.mailer.Mailer]] itself
		*/
	@throws[MessagingException]
	def connect(): Mailer

	/**
		* Sends the given message.
		*
		* @param msg message to send
		* @return instance of the [[com.github.jurajburian.mailer.Mailer]] itself
		*/
	@throws[MessagingException]
	def send(msg: Message): Mailer

	/**
		* Returns the instance of `javax.mail.Transport`, used by this instance of 'Mailer'
		*
		* @return instance of `javax.mail.Transport`
		*/
	def transport: Transport

	/**
		* Closes the previously opened transport connection.
		*
		* @return instance of the [[com.github.jurajburian.mailer.Mailer]] itself
		*/
	@throws[MessagingException]
	def close(): Mailer
}


/**
	* ''Mailer'' object providing default operations to handle the transport connection and send the
	* e-mail message.
	*
	* @author jubu
	*/
object Mailer {

	import MailKeys._

	/**
		* Sets the ''JavaMail'' session to the ''Mailer'' and returns the instance ready to send e-mail
		* messages. Optionally, transport method can be explicitly specified.
		*
		* @param session      ''JavaMail'' session
		* @param transportOpt transport method (optional)
		* @return ''Mailer'' instance
		*/
	def apply(session: Session, transportOpt: Option[Transport] = None) = new Mailer {


		val trt = transportOpt match {
			case None => if (session.getProperty(TransportProtocolKey) == null) {
				session.getTransport("smtp")
			} else {
				session.getTransport
			}
			case Some(t) => t
		}

		@throws[MessagingException]
		override def connect(): Mailer = {
			if (!trt.isConnected) {
				trt.connect()
			}
			this
		}

		@throws[MessagingException]
		override def send(msg: Message): Mailer = {
			import jakarta.mail.{Message => M}
			connect()
			val message = new MimeMessage(session)
			msg.to.foreach(message.addRecipient(M.RecipientType.TO, _))
			msg.cc.foreach(message.addRecipient(M.RecipientType.CC, _))
			msg.bcc.foreach(message.addRecipient(M.RecipientType.BCC, _))
			msg.headers.foreach(header => message.setHeader(header.name, header.value))
			message.setSubject(msg.subject)
			message.setFrom(msg.from)
			message.setContent(new MimeMultipart() {
				msg.content.parts.foreach(addBodyPart(_))
			})
			trt.sendMessage(message, message.getAllRecipients)
			this
		}

		override def transport: Transport = trt

		@throws[MessagingException]
		override def close(): Mailer = {
			if (trt.isConnected) {
				trt.close()
			}
			this
		}
	}
}