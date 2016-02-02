package jubu.mailer

trait MailKeys {
	val SmtpConnectionTimeoutKey = "mail.smtp.connectiontimeout"
	val SmtpFromKey = "mail.smtp.from"
	val SmtpHostKey = "mail.smtp.host"
	val SmtpPortKey = "mail.smtp.port"
	val SmtpTimeoutKey = "mail.smtp.timeout"

	val Debug = "mail.debug"
	val From = "mail.from"
	val TransportProtocolKey = "mail.transport.protocol"
	val MimeAddressStrict = "mail.mime.address.strict"

}

trait Prop extends MailKeys {

	/**
		*
		* @return sequence of key value pairs
		*/
	def convert: Seq[(String, _)]

	/**
		*
		* @return sequence of keys in given property
		*/
	def keys = convert.map(_._1)

}

/**
	*
	* @param host The SMTP server to connect to.
	* @param port The SMTP server port to connect to, if the connect() method doesn't explicitly specify one. Defaults to 25.
	*/
case class SmtpAddress(host: String, port: Int = 25) extends Prop {
	override def convert = Seq(SmtpHostKey -> host, SmtpPortKey -> port)
}

/**
	*
	* @param timeout Socket connection timeout value in milliseconds. Default is infinite timeout.
	*/
case class SmtpConnectionTimeout(timeout: Int) extends Prop {
	override def convert = Seq(SmtpConnectionTimeoutKey -> timeout)
}

/**
	*
	* @param from Email address to use for SMTP MAIL command. This sets the envelope return address. Defaults to msg.getFrom() or InternetAddress.getLocalAddress().
	*/
case class SmtpFrom(from: String) extends Prop {
	override def convert = Seq(SmtpFromKey -> from)
}

/**
	*
	* @param timeout Socket I/O timeout value in milliseconds. Default is infinite timeout.
	*/
case class SmtpTimeout(timeout: Int) extends Prop {
	override def convert = Seq(SmtpTimeoutKey -> timeout)
}

/**
	*
	* @param protocol Specifies the default message transport protocol. The Session method getTransport() returns a Transport object that implements this protocol. By default the first Transport provider in the configuration files is returned.
	*/
case class TransportProtocol(protocol: String = "smtp") extends Prop {
	override def convert = Seq(TransportProtocolKey -> protocol)
}

/**
	*
	* @param debug The initial debug mode. Default is false.
	*/
case class Debug(debug: Boolean = false) extends Prop {
	override def convert = Seq(Debug -> debug.toString)
}

case class From(from: String) extends Prop {
	override def convert = Seq(From -> from)
}

/**
	*
	* @param mimeAddressStrict The MimeMessage class uses the InternetAddress method parseHeader to parse headers in messages. This property controls the strict flag passed to the parseHeader method. The default is true.
	*/
case class MimeAddressStrict(mimeAddressStrict: String) extends Prop {
	override def convert = Seq(MimeAddressStrict -> mimeAddressStrict)
}