# Minecart Train Speed

Makes **linked** minecarts obey [Modern Minecarts](https://www.curseforge.com/minecraft/mc-mods/modern-minecarts)'
`copper_speed` config instead of a hardcoded value.

Minecraft 1.20.1 / Forge. Requires Modern Minecarts. MIT.

## The bug this works around

`CustomAbstractMinecartEntity.getMaxSpeedWithRail` works out the speed limit for the rail under
the cart, consulting the mod's own config for powered rails. It then reaches a branch for linked
carts which — unlike every branch before it — assigns a **literal `0.8`** rather than reading the
config. Confirmed by disassembling the method: the constant appears there exactly once.

`0.8` happens to equal the default `copper_speed`, so **with default settings there is no visible
difference**. If you raise `copper_speed`, every linked cart silently drops back to 0.8 — a train
becomes slower than a single cart on the same rail.

## What this mod does

One `@ModifyConstant` replaces that `0.8f` with a configurable value that defaults to following
`copper_speed`.

`0.8f` appears **exactly once** in that method (verified by disassembling the jar), so the
injection cannot hit anything else. `remap = false` throughout — the target is another mod's
class, not a Minecraft one, so the names must not be mapped to SRG.

## Config

`config/minecart_train_speed-common.toml` — one key: the maximum speed for linked carts, in
blocks per tick. **Zero or less (the default) follows Modern Minecarts' `copper_speed`.**

## Build

```bash
export JAVA_HOME=/path/to/jdk-17      # JDK 17 ならどれでもよい
./gradlew build --no-daemon
# → build/libs/minecart_train_speed-<version>.jar
```

## Status

This is a workaround, not a feature. It has been reported upstream; **if Modern Minecarts reads
its own config there, this mod becomes unnecessary** and will be retired.

## Contributing

Issues and pull requests are welcome.
