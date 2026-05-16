# Blocked Servers Host/IP Entries

This page lists the default host/IP entries this mod treats as allowlisted overrides against Mojang host blocking checks.

## Featured auto-add entries

These are also candidates for automatic insertion into the multiplayer server list:

| Server | Host/IP entry |
|---|---|
| 6b6t | `6b6t.org` |
| 10b10t | `10b10t.org` |
| SimpleAnarchy | `simpleanarchy.org` |

## Default host pattern coverage

The following exact and wildcard entries are bundled by default:

- `*.6b6t.org`, `6b6t.org`
- `*.10b10t.org`, `10b10t.org`
- `*.6b6t.cc`, `6b6t.cc`
- `*.6b6t.me`, `6b6t.me`
- `*.7b7t.me`, `7b7t.me`
- `*.8b8t.org`, `8b8t.org`
- `*.alacity.net`, `alacity.net`
- `*.anarchypvp.pw`, `anarchypvp.pw`
- `*.l2x9.org`, `l2x9.org`
- `*.simpleanarchy.org`, `simpleanarchy.org`

## Remote updates

At startup, the mod attempts to load additional domains (and optional featured server entries) from:

`https://www.6b6t.org/api/anarchy-mod.json`
