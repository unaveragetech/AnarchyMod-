# Bypass Overview

This mod's position is that Mojang's central host blocking is overbroad and can prevent legitimate player communities from being reached.

## How the bypass works

1. Minecraft asks `BlockedServers` whether a host should be denied.
2. `BlockedServersMixin` intercepts that answer at return time.
3. If the host matches a configured domain pattern in `Domains`, the mixin forces the answer to `false`.
4. Because the client now treats the host as allowed, normal connection flow continues and the player can join.

## Why players can still join blocked hosts

The block decision is made on the client side by host-name matching.  
AnarchyMod does not change server authentication or session ownership; it only changes the local host-block verdict for matched entries.

## Auto-add and preview flow

- `ServerListMixin` gathers current saved entries.
- It asks `Domains` for featured servers that are still missing.
- The mod logs the pending list before insertion (`Pending auto-added servers: ...`).
- Missing entries are inserted at the top of the in-game server list.
