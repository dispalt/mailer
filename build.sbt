val pScalaVersion = "2.13.16"

organization := "com.dispalt"

name := "mailer"

version := "2.1.2"

description := "Thin wrapper of JavaMail library written in Scala language. Mailer is aim to be used in situations when is necessary send multiple mails, e.t. instance of javax.mail.Session is created and used by Mailer."

scalaVersion in Scope.GlobalScope := pScalaVersion

crossScalaVersions := Seq("2.12.20", pScalaVersion, "3.3.6")

ThisBuild / publishTo := {
  val centralSnapshots =
    "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

autoAPIMappings := true

Test / publishArtifact := false

pomIncludeRepository := { _ => false }

pomExtra := (<url>https://github.com/JurajBurian/mailer</url>
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

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, _)) =>
      Seq(
        "-encoding",
        "UTF-8",
        "-unchecked",
        "-deprecation",
        "-feature",
        "-Xfatal-warnings",
        "-Xlint",
        "-Yrangepos",
        "-language:postfixOps",
        "-release",
        "8"
      )
    case _ =>
      Seq.empty

  }
}

libraryDependencies ++= Seq(
  "jakarta.mail" % "jakarta.mail-api" % "2.1.3",
  "org.eclipse.angus" % "angus-mail" % "2.0.3",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)
