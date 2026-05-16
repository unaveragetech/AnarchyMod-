# AnarchyMod

[![Build](https://github.com/6b6t/AnarchyMod/actions/workflows/build.yml/badge.svg)](https://github.com/6b6t/AnarchyMod/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

🔓 Client-side unblock support for communities that are impacted by Mojang's blocked-server host list.

## Features

- **Server-type agnostic unblocking** — only checks host/domain entries, so it works regardless of gameplay type
- **Auto server list population** — adds featured community servers if they are missing from your list
- **Pre-add preview logging** — logs every server that is queued before auto-add insertion occurs
- **Join notification** — sends a lightweight packet when connecting to a matched host

## Documentation

- [Bypass overview](docs/BYPASS_OVERVIEW.md)
- [Blocked server host/IP list](docs/BLOCKED_SERVERS.md)

## Supported Minecraft Versions

| Minecraft | Module       |
|-----------|--------------|
| 1.19.4    | `mc-1.19.4`  |
| 1.20      | `mc-1.20`    |
| 1.20.1    | `mc-1.20.1`  |
| 1.20.2    | `mc-1.20.2`  |
| 1.20.3    | `mc-1.20.3`  |
| 1.20.4    | `mc-1.20.4`  |
| 1.20.5    | `mc-1.20.5`  |
| 1.20.6    | `mc-1.20.6`  |
| 1.21      | `mc-1.21`    |
| 1.21.1    | `mc-1.21.1`  |
| 1.21.2    | `mc-1.21.2`  |
| 1.21.3    | `mc-1.21.3`  |
| 1.21.4    | `mc-1.21.4`  |
| 1.21.5    | `mc-1.21.5`  |
| 1.21.6    | `mc-1.21.6`  |
| 1.21.7    | `mc-1.21.7`  |
| 1.21.8    | `mc-1.21.8`  |
| 1.21.9    | `mc-1.21.9`  |
| 1.21.10   | `mc-1.21.10` |
| 1.21.11   | `mc-1.21.11` |

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api) for your
   Minecraft version
2. Download the matching JAR from [Releases](https://github.com/6b6t/AnarchyMod/releases)
3. Place the JAR in your `.minecraft/mods` folder

## Building from Source

Requires Java 21+.

```sh
git clone https://github.com/6b6t/AnarchyMod.git
cd AnarchyMod
./gradlew chiseledBuild
```

Built JARs are located in each version module's `build/libs/` directory.

## License

[MIT](LICENSE)
