# Totem Only Offhand

A client-side Fabric mod: only totems of undying can go in your offhand.

Blocks inventory clicks, number-key swaps, shift-click shields and the F-key swap
when the item isn't a totem.

## Build

Requires Java 21 and Gradle 8.14.3+.

```
gradle wrapper --gradle-version 8.14.3   # one time, generates ./gradlew
./gradlew build
```

The jar is in `build/libs/`. Or push to GitHub and download it from the Actions tab.

Edit `gradle.properties` if you're on a different Minecraft version
(see https://fabricmc.net/develop).
