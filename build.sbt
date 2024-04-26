val pScalaVersion = "2.13.13"

organization := "com.dispalt"

name := "mailer"

version := "2.1.0"

description := "Thin wrapper of JavaMail library written in Scala language. Mailer is aim to be used in situations when is necessary send multiple mails, e.t. instance of javax.mail.Session is created and used by Mailer."

scalaVersion in Scope.GlobalScope := pScalaVersion

crossScalaVersions := Seq("2.12.18", pScalaVersion)

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

autoAPIMappings := true

Test / publishArtifact := false

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
	"-language:postfixOps",
	"-release", "8"
)

libraryDependencies ++= Seq(
	"org.eclipse.angus" % "jakarta.mail" % "2.0.3",
	"org.scalatest" %% "scalatest" % "3.2.18" % "test"
)
