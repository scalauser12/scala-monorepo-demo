ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.8.1"

lazy val common = (project in file("common"))
  .settings(name := "demo-common")

lazy val core = (project in file("core"))
  .dependsOn(common)
  .settings(name := "demo-core")

lazy val api = (project in file("api"))
  .dependsOn(core)
  .settings(name := "demo-api")

lazy val cli = (project in file("cli"))
  .dependsOn(core)
  .settings(name := "demo-cli")

lazy val root = (project in file("."))
  .aggregate(common, core, api, cli)
  .enablePlugins(MonorepoReleasePlugin)
  .settings(
    name := "scala-monorepo-demo",
    publish / skip := true,
    releaseIOMonorepoDetectChanges := true,
    releaseIOIgnoreUntrackedFiles := true,
    // Remove push and publish for local demo
    releaseIOMonorepoProcess := releaseIOMonorepoProcess.value.filterNot { step =>
      step.name == "push-changes" || step.name == "publish-artifacts"
    }
  )
