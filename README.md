# scala-monorepo-demo

A demo project showcasing the [sbt-release-io-monorepo](https://github.com/scalauser12/sbt-release-io/blob/main/modules/monorepo/README.md) plugin for independent versioning and releasing of subprojects in an sbt monorepo.

## Prerequisites

- JDK 11+
- sbt
- git

## Project Structure

```
scala-monorepo-demo/
├── common/          # Shared utilities (no dependencies)
│   ├── version.sbt
│   └── CHANGELOG.md
├── core/            # Core logic (depends on common)
│   ├── version.sbt
│   └── CHANGELOG.md
├── api/             # HTTP API (depends on core)
│   ├── version.sbt
│   └── CHANGELOG.md
├── cli/             # CLI tool (depends on core)
│   ├── version.sbt
│   └── CHANGELOG.md
├── build.sbt
└── project/
    ├── build.properties
    └── plugins.sbt
```

Dependency graph:

```
common ─> core ─┬─> api
                └─> cli
```

Each subproject maintains its own `version.sbt` file, allowing independent version bumps.

## Plugin Setup

### plugins.sbt

```scala
addSbtPlugin("io.github.scalauser12" % "sbt-release-io-monorepo" % "0.9.2")
```

### build.sbt

The root project aggregates all subprojects and enables `MonorepoReleasePlugin`:

```scala
lazy val root = (project in file("."))
  .aggregate(common, core, api, cli)
  .enablePlugins(MonorepoReleasePlugin)
  .settings(
    name := "scala-monorepo-demo",
    publish / skip := true,
    releaseIOMonorepoDetectionEnabled := true,
    releaseIOVcsIgnoreUntrackedFiles := true,
    releaseIOMonorepoDetectionIncludeDownstream := true,
    releaseIOMonorepoDetectionExcludes :=
      Seq("common", "core", "api", "cli").map(baseDirectory.value / _ / "CHANGELOG.md"),
    releaseIOMonorepoVcsReleaseCommitMessage := (summary => s"release: $summary"),
    releaseIOMonorepoVcsNextCommitMessage := (summary => s"chore: bump to next snapshot ($summary)"),
    releaseIOMonorepoVcsTagName := ((name, ver) => s"$name-v$ver"),
    releaseIOMonorepoVcsTagComment := ((name, ver) => s"Release $name version $ver"),
    releaseIOMonorepoPolicyEnablePush := false,
    releaseIOMonorepoPolicyEnablePublish := false
  )
```

Key settings:

| Setting | Value | Description |
|---------|-------|-------------|
| `releaseIOMonorepoDetectionEnabled` | `true` | Uses git to detect which subprojects have changed since their last release tag |
| `releaseIOVcsIgnoreUntrackedFiles` | `true` | Allows releasing with untracked files in the working directory |
| `releaseIOMonorepoDetectionIncludeDownstream` | `true` | Automatically includes transitive dependents of changed projects in the release |
| `releaseIOMonorepoDetectionExcludes` | CHANGELOG.md per subproject | Files excluded from git-based change detection |
| `releaseIOMonorepoVcsReleaseCommitMessage` | `summary => s"release: $summary"` | Custom format for release version commit messages |
| `releaseIOMonorepoVcsNextCommitMessage` | `summary => s"chore: bump to next snapshot ($summary)"` | Custom format for next snapshot version commit messages |
| `releaseIOMonorepoVcsTagName` | `(name, ver) => s"$name-v$ver"` | Custom tag name format (default: `<project>/v<version>`) |
| `releaseIOMonorepoVcsTagComment` | `(name, ver) => s"Release $name version $ver"` | Custom annotated tag message |
| `releaseIOMonorepoPolicyEnablePush` | `false` | Disables the `push-changes` step so the demo stays local |
| `releaseIOMonorepoPolicyEnablePublish` | `false` | Disables the `publish-artifacts` step so the demo stays local |

The `push-changes` and `publish-artifacts` steps are disabled so the demo runs entirely locally.

Current releases use grouped monorepo setting names such as `releaseIOMonorepoDetection*`,
`releaseIOMonorepoVcs*`, and `releaseIOMonorepoPolicy*`.

## Running a Release

```bash
sbt "releaseIOMonorepo check with-defaults"
```

Then run the release locally:

```bash
sbt "releaseIOMonorepo with-defaults"
```

The plugin will:

1. Check for a clean working directory
2. Resolve the release order based on the dependency graph (`common -> core -> cli -> api`)
3. Detect which projects have changed since their last release tag (e.g. `common-v0.1.0`)
4. If `releaseIOMonorepoDetectionIncludeDownstream` is enabled, include all downstream dependents of changed projects
5. For each selected project: check for snapshot dependencies, compute release and next versions
6. Run `clean` and `test` for each selected project
7. Write release versions to each project's `version.sbt`, commit
8. Create per-project tags (e.g. `common-v0.1.0`, `core-v0.1.0`)
9. Write next snapshot versions, commit

### Releasing Specific Projects

```bash
# Release only core and api
sbt "releaseIOMonorepo core api with-defaults"

# Override a specific project's release version
sbt "releaseIOMonorepo with-defaults release-version core=2.0.0"
```

## Features Demonstrated

### Git-Based Change Detection

The plugin runs `git describe` and `git diff` to find which projects have file changes since their last release tag. On a first release (no tags exist), all projects are marked as changed.

Changes to shared paths (`build.sbt`, `project/`) trigger all projects.

### Dependency-Aware Release Ordering

Projects are topologically sorted so dependencies are always released before dependents. In this demo: `common` is released first, then `core`, then `api` and `cli`.

### Downstream Inclusion

With `releaseIOMonorepoDetectionIncludeDownstream := true`, changing only `common` will automatically include `core`, `api`, and `cli` in the release, since they transitively depend on it.

### Change Detection Excludes

CHANGELOG.md files are excluded from change detection via `releaseIOMonorepoDetectionExcludes`. Editing a subproject's CHANGELOG won't trigger a release for that project. This is useful for files that change alongside releases but shouldn't cause re-releases of downstream dependents.

### Custom Commit Messages and Tags

Commit messages and tag formats are customizable via settings. This demo uses conventional commit prefixes (`release:`, `chore:`) and a hyphenated tag format (`core-v0.1.0` instead of the default `core/v0.1.0`).

### Per-Project Tagging

Each project gets its own tag in the format `<project>-v<version>` (e.g. `core-v0.1.0`). This enables independent version tracking and change detection per subproject.

## Additional Configuration

The plugin supports many more settings not used in this demo:

| Setting | Default | Description |
|---------|---------|-------------|
| `releaseIOMonorepoBehaviorSkipTests` | `false` | Skip the test step |
| `releaseIOMonorepoBehaviorCrossBuild` | `false` | Enable cross-building |
| `releaseIOMonorepoDetectionSharedPaths` | `Seq("build.sbt", "project/")` | Root paths that trigger all projects when changed |
| `releaseIOMonorepoBehaviorInteractive` | `false` | Prompt interactively for versions during `inquire-versions` |
| `releaseIOMonorepoPolicyEnableRunClean` | `true` | Include `run-clean` in the compiled process |

See the [plugin documentation](https://github.com/scalauser12/sbt-release-io/blob/main/modules/monorepo/README.md) for the full configuration reference.
