val pScalaVersion = "2.13.3"

organization := "com.dispalt"

name := "mailer"

version := "1.2.5"

description := "Thin wrapper of JavaMail library written in Scala language. Mailer is aim to be used in situations when is necessary send multiple mails, e.t. instance of javax.mail.Session is created and used by Mailer."

scalaVersion in Scope.GlobalScope := pScalaVersion

crossScalaVersions := Seq("2.11.8", pScalaVersion)

publishMavenStyle := true

publishTo := {
	val nexus = "https://oss.sonatype.org/"
	if (isSnapshot.value)
		Some("snapshots" at nexus + "content/repositories/snapshots")
	else
		Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishTo := sonatypePublishToBundle.value

// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "com.dispalt"

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

// Open-source license of your choice
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
	<url>https://github.com/JurajBurian/mailer</url>
		<licenses>
			<license>
				<name>unlicense</name>
				<url>http://unlicense.org/</url>
				<distribution>repo</distribution>
			</license>
		</licenses>
		<scm>
			<url>https://github.com/jurajburian/mailer</url>
			<connection>scm:git:https://github.com/jurajburian/mailer</connection>
		</scm>
		<developers>
			<developer>
				<id>JurajBurian</id>
				<name>Juraj Burian</name>
				<url>https://github.com/JurajBurian</url>
			</developer>
			<developer>
				<id>vaclavsvejcar</id>
				<name>Vaclav Svejcar</name>
				<url>https://github.com/vaclavsvejcar</url>
			</developer>
			<developer>
				<id>basert</id>
				<name>Fabian Gruber</name>
				<url>https://github.com/basert</url>
			</developer>
		</developers>)

scalacOptions := Seq(
	"-encoding", "UTF-8",
	"-unchecked",
	"-deprecation",
	"-feature",
	"-Xfatal-warnings",
	"-Xlint",
	"-Yrangepos",
	"-language:postfixOps"
)

libraryDependencies ++= Seq(
	"javax.mail" % "mail" % "1.4.7",
	"de.saly" % "javamail-mock2-fullmock" % "0.5-beta4" % "test",
	"org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % "test"
)
