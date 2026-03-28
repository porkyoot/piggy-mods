# Piggy Mods

A collection of Minecraft mods by [porkyoot](https://github.com/porkyoot).

| Mod | Description | Build Status | Test Status |
| :--- | :--- | :--- | :--- |
| [piggy-admin](https://github.com/porkyoot/piggy-admin/) | Advanced administration with AI moderation, anti-cheat enforcement, and forensic logging. | [![Build Status](https://github.com/porkyoot/piggy-admin/actions/workflows/build.yml/badge.svg)](https://github.com/porkyoot/piggy-admin/actions/workflows/build.yml) | [![test](https://github.com/porkyoot/piggy-admin/actions/workflows/test.yml/badge.svg)](https://github.com/porkyoot/piggy-admin/actions/workflows/test.yml) |
| [piggy-build](https://github.com/porkyoot/piggy-build/) | Building utility suite with shape guides, Auto-MLG, and performance-aware automation. | [![Build Status](https://github.com/porkyoot/piggy-build/actions/workflows/build.yml/badge.svg)](https://github.com/porkyoot/piggy-build/actions/workflows/build.yml) | [![test](https://github.com/porkyoot/piggy-build/actions/workflows/test.yml/badge.svg)](https://github.com/porkyoot/piggy-build/actions/workflows/test.yml) |
| [piggy-inventory](https://github.com/porkyoot/piggy-inventory/) | Intelligent inventory management with mega-stack sorting and forensic telemetry. | [![Build Status](https://github.com/porkyoot/piggy-inventory/actions/workflows/build.yml/badge.svg)](https://github.com/porkyoot/piggy-inventory/actions/workflows/build.yml) | [![test](https://github.com/porkyoot/piggy-inventory/actions/workflows/test.yml/badge.svg)](https://github.com/porkyoot/piggy-inventory/actions/workflows/test.yml) |
| [piggy-lib](https://github.com/porkyoot/piggy-lib/) | Shared foundation, networking, and the central Piggy Action Queue for all mods. | [![Build Status](https://github.com/porkyoot/piggy-lib/actions/workflows/build.yml/badge.svg)](https://github.com/porkyoot/piggy-lib/actions/workflows/build.yml) | N/A |

## 🐷 The Piggy Foundation

All mods in this suite are built upon **Piggy Lib**, a shared architectural foundation that ensures stability, performance, and anti-cheat compliance.

### ⚡ Centralized Action Queue
The suite uses a **Stateful Action Queue** to manage all player interactions. Instead of direct interaction, actions are queued, rate-limited, and verified to prevent race conditions and ensure a natural, human-like interaction frequency.

### 📊 Forensic Telemetry
Every complex operation (like sorting or MLG survival) is wrapped in a **Meta-Action Session**, providing rich diagnostic telemetry and detailed forensic reports in case of failure.

### 🛡️ Unified Anti-Cheat
Server-side settings are automatically synchronized to all connected clients, ensuring that advanced automation features stay within the bounds defined by the server administrator.
