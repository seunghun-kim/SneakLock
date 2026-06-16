# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.3.0] - 2026-06-16

Update for Minecraft 26.1.2.

### Changed
- Updated Minecraft to **26.1.2** (Fabric Loader 0.19.3, Fabric API 0.152.1+26.1.2).
- Updated the build toolchain: Fabric Loom 1.16-SNAPSHOT, Gradle 9.4.1, and Java 25
  (now the minimum required by Minecraft 26.1).
- **Migrated from Yarn mappings to official Mojang mappings.** Minecraft 26.1 is the
  first unobfuscated release and Fabric has dropped Yarn/Intermediary, so the mod now
  builds against the official names:
  - `MinecraftClient` → `Minecraft`
  - `InputUtil.isKeyPressed(Window, key)` → `InputConstants.isKeyDown(Window, key)`
  - `Text` / `Text.literal(...)` → `Component` / `Component.literal(...)`
  - `player.sendMessage(text, true)` (action bar) → `player.sendOverlayMessage(Component)`
  - `KeyBinding` → `KeyMapping`; mixin targets `isPressed` → `isDown` and
    `wasPressed()` → `consumeClick()`, and the `id` field → `name`.
- Switched dependencies from `modImplementation` to `implementation` (no remapping is
  needed for an unobfuscated game) and changed the Loom plugin id to
  `net.fabricmc.fabric-loom`.
- Raised the Fabric mixin `compatibilityLevel` to `JAVA_25`.
- Declared `java >= 25` and `fabric-api` in the `fabric.mod.json` dependencies.

### Added
- Gradle wrapper (`gradlew`, `gradle-wrapper.jar`), which was missing from the repository.

## [0.2.0] - 2026-01-19

### Changed
- Updated for Minecraft 1.21.11 (Fabric Loader 0.18.4, Fabric API 0.141.1+1.21.11,
  Yarn 1.21.11+build.4, Loom 1.14-SNAPSHOT, Gradle 9.2.1).
- Adapted to API changes: `InputUtil.isKeyPressed` now takes a `Window`, and
  `KeyBinding.getTranslationKey()` was replaced with the `id` field.

## [0.1.1] - 2025-06-09

### Changed
- Updated for Minecraft 1.21.5.

## [0.1.0] - 2025-04-29

### Added
- Initial release: toggle sneak by pressing both Shift keys, prevent jumping while the
  sneak lock is active, and show action-bar notifications for both actions.
