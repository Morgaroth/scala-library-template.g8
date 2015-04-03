import SbtReleaseHelpers._
import sbtbuildinfo.Plugin._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.ReleaseStep
import sbtrelease.ReleasePlugin.ReleaseKeys.crossBuild

name := """$name$"""

scalacOptions ++= Seq("-feature")

organization := """$organization$"""

crossScalaVersions := Seq("2.10.4", "2.11.6")

scalaVersion := "2.11.6"

resolvers ++= Seq(
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
)

buildInfoSettings

buildInfoKeys := Seq[BuildInfoKey](
  name, version, scalaVersion, sbtVersion, libraryDependencies, resolvers
)

buildInfoPackage := "$package$.build"

sourceGenerators in Compile <+= buildInfo

sonatypeSettings

releaseSettings

crossBuild := true

ReleaseKeys.releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies, // : ReleaseStep
  inquireVersions, // : ReleaseStep
  runClean,
  runTest, // : ReleaseStep
  setReleaseVersion, // : ReleaseStep
  publishArtifactsSigned,
  finishReleaseAtSonatype,
  commitReleaseVersion, // : ReleaseStep, performs the initial git checks
  tagRelease, // : ReleaseStep
  setNextVersion, // : ReleaseStep
  commitNextVersion, // : ReleaseStep
  pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
)

publishArtifact in Test := false

pomExtra := githubPom(name.value,"$github_user_name$","$github_user_id$")

publishTo := publishRepoForVersion(version.value)

// Do not include log4jdbc as a dependency.
pomPostProcess := PackagingHelpers.removeTestOrSourceDependencies
