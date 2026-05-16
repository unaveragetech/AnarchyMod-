# AnarchyMod

[![Build](https://github.com/6b6t/AnarchyMod/actions/workflows/build.yml/badge.svg)](https://github.com/6b6t/AnarchyMod/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Client-side Fabric mod that bypasses Mojang host blocking for configured domains and auto-adds featured multiplayer entries.

## What it does

- Overrides blocked-host checks for configured domain patterns
- Auto-populates known featured servers into the multiplayer list when missing
- Sends a lightweight join payload when connecting to matched servers
- Supports many Minecraft versions through Stonecutter multi-version modules

## How it works (code map)

### Entrypoint and runtime flow

1. Fabric loads [`AnarchyMod`](src/main/java/net/blockhost/anarchymod/AnarchyMod.java) from [`fabric.mod.json`](src/main/resources/fabric.mod.json).
2. `AnarchyMod` registers [`JoinPayload`](src/main/java/net/blockhost/anarchymod/JoinPayload.java) and hooks `ClientPlayConnectionEvents.JOIN`.
3. On join, if the current server IP/domain is matched by [`Domains.contains`](src/main/java/net/blockhost/anarchymod/Domains.java), the client sends `JoinPayload`.

### Mixin hooks

- [`BlockedServersMixin`](src/main/java/net/blockhost/anarchymod/mixin/BlockedServersMixin.java) injects into Mojang `BlockedServers.isBlockedServerHostName` and forces `false` for allowlisted domains.
- [`ServerListMixin`](src/main/java/net/blockhost/anarchymod/mixin/ServerListMixin.java) injects after server list load and inserts missing featured servers at the top of the list.
- Mixin wiring lives in [`anarchymod.mixins.json`](src/main/resources/anarchymod.mixins.json).

### Domain and featured server data

- [`Domains`](src/main/java/net/blockhost/anarchymod/Domains.java) contains:
  - default exact/wildcard domain patterns
  - default featured servers
  - normalization + matching logic for domains, host:port, and bracketed IPv6 forms
  - optional remote JSON merge from `https://www.6b6t.org/api/anarchy-mod.json`
- See docs:
  - [Bypass overview](docs/BYPASS_OVERVIEW.md)
  - [Blocked/featured entries](docs/BLOCKED_SERVERS.md)

## Repository layout

- Build and versioning
  - [`settings.gradle.kts`](settings.gradle.kts): Stonecutter versions matrix
  - [`stonecutter.gradle.kts`](stonecutter.gradle.kts): active version + `chiseledBuild`
  - [`gradle/stonecutter-maven`](gradle/stonecutter-maven): vendored Stonecutter 0.9.4 artifacts and sources
  - [`build.gradle.kts`](build.gradle.kts): Loom build, dependencies, resource expansion
  - [`gradle.properties`](gradle.properties): global mod metadata/version
  - [`versions/*/gradle.properties`](versions): per-Minecraft loader/API/Java settings
- Resources
  - [`fabric.mod.json`](src/main/resources/fabric.mod.json): Fabric metadata/entrypoint/deps
  - [`anarchymod.mixins.json`](src/main/resources/anarchymod.mixins.json): mixin declarations
  - [`icon.png`](src/main/resources/assets/anarchymod/icon.png): mod icon
- Automation
  - [Build workflow](.github/workflows/build.yml): CI build + artifact upload for all versions
  - [Release workflow](.github/workflows/release.yml): version bump, build, release notes, GitHub release publish
  - [Set version workflow](.github/workflows/set-version.yml): updates `mod_version` in `gradle.properties`

## Supported Minecraft versions

`1.19.4`, `1.20`, `1.20.1`, `1.20.2`, `1.20.3`, `1.20.4`, `1.20.5`, `1.20.6`, `1.21`, `1.21.1`, `1.21.2`, `1.21.3`, `1.21.4`, `1.21.5`, `1.21.6`, `1.21.7`, `1.21.8`, `1.21.9`, `1.21.10`, `1.21.11`

## Build from source

Requires Java 21+.

```sh
git clone https://github.com/6b6t/AnarchyMod.git
cd AnarchyMod
./gradlew chiseledBuild
```

The repository keeps a local Stonecutter 0.9.4 copy under [`gradle/stonecutter-maven`](gradle/stonecutter-maven). If those files are ever missing, `settings.gradle.kts` automatically re-downloads the required artifacts from the upstream Stonecutter distribution while the source project remains hosted at <https://codeberg.org/stonecutter/stonecutter>.

Output jars are emitted in each version module under `versions/<mc-version>/build/libs/`.

## Release process

Releases are produced by GitHub Actions:

1. Run [`Publish Release`](.github/workflows/release.yml) (`workflow_dispatch`).
2. Provide:
   - `version`: release version (for example `1.1.1`)
   - `after-version`: next snapshot (for example `1.1.2-SNAPSHOT`)
3. Workflow bumps version, builds all modules, generates changelog, and publishes GitHub release assets.

## License

[MIT](LICENSE)
