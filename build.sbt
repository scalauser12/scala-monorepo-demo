ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "3.8.2"
ThisBuild / libraryDependencies += "org.scalameta" %% "munit" % "1.2.4" % Test

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
    releaseIOMonorepoIncludeDownstream := true,
    releaseIOMonorepoDetectChangesExcludes :=
      Seq("common", "core", "api", "cli").map(baseDirectory.value / _ / "CHANGELOG.md"),
    releaseIOMonorepoCommitMessage := (summary => s"release: $summary"),
    releaseIOMonorepoNextCommitMessage := (summary => s"chore: bump to next snapshot ($summary)"),
    releaseIOMonorepoTagName := ((name, ver) => s"$name-v$ver"),
    releaseIOMonorepoTagComment := ((name, ver) => s"Release $name version $ver"),
    // Disable remote side effects for the local demo.
    releaseIOMonorepoEnablePush := false,
    releaseIOMonorepoEnablePublish := false
  )
