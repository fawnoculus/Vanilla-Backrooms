# Vanilla-Backrooms
[![Available on Fabric](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/supported/fabric_64h.png)](https://fabricmc.net/)
[![Available on Quilt](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3.3.1/assets/cozy/supported/quilt_64h.png)](https://quiltmc.net/)

[![Licensed under: GPL-3.0](https://img.shields.io/github/license/fawnoculus/Vanilla-Backrooms?style=flat&color=900c3f)](https://github.com/fawnoculus/Vanilla-Backrooms/blob/master/LICENSE.txt)
[![Build Workflow](https://github.com/fawnoculus/Vanilla-Backrooms/actions/workflows/build.yml/badge.svg)](https://github.com/fawnoculus/Vanilla-Backrooms/actions/workflows/build.yml)

TLDR: Vanilla-Backrooms is a serverside only Backrooms mod based on info from the [Backrooms Wiki](https://backrooms-wiki.wikidot.com/start).

*Ever wanted to put your friends in the backrooms, but it would be way too suspicious to make them download a mod?*

**Well then this mod is the solution off your problem!**

This mod, which only has to be installed on the server will bring them to the backrooms without them having to download a mod.
They won't even know what happened when they suddenly clip into the backrooms!

### Vanilla-Backrooms currently has these Levels:
- [Level-0](https://backrooms-wiki.wikidot.com/level-0): Tutorial Level
- [Level-1](https://backrooms-wiki.wikidot.com/level-1): Habitable Zone
- [Level-2](https://backrooms-wiki.wikidot.com/level-2): Abandoned Utility Halls
- [Level-3](https://backrooms-wiki.wikidot.com/level-3): Electrical Station
- [Level-4](https://backrooms-wiki.wikidot.com/level-4): Abandoned Office
- [Level-5](https://backrooms-wiki.wikidot.com/level-5): Terror Hotel
More Levels are in development!

### Vanilla-Backrooms currently has these Object:
- [Object-1](https://backrooms-wiki.wikidot.com/object-1): Almond Water
- [Object-2](https://backrooms-wiki.wikidot.com/object-2): Level Keys
- [Object-28](https://backrooms-wiki.wikidot.com/object-28): Lucky O’ Milk
More Objects are in development!

## Installing Vanilla-Backrooms
- ~~Modrinth~~ (maybe someday)
- ~~CurseForge~~ (maybe someday)
- [GitHub Releases](https://github.com/fawnoculus/Vanilla-Backrooms/releases/latest)
- [GitHub Actions](https://github.com/fawnoculus/Vanilla-Backrooms/actions/workflows/build.yml) (refer to the Section Below)
- Building it from Source (refer to the Section Below)

## Downloading from GitHub actions
1. Navigate to the Latest (topmost) successfully ran (green check) [Action](https://github.com/fawnoculus/Vanilla-Backrooms/actions/workflows/build.yml)
2. Click on that bad boy
3. On the bottom right there should be a button for downloading the Artifact (you may need to scroll down)
4. If you unzip the File you should now have a working version of the mod (you don't want the file that ends in "-sources.jar")

## Building it from Source
Building it from source should be unnecessary as you can download a jar of the latest commit from [GitHub Actions](https://github.com/fawnoculus/Vanilla-Backrooms/actions/workflows/build.yml)
* Make sure you have [**JDK-21**](https://adoptium.net/temurin/releases/?variant=openjdk8&jvmVariant=hotspot&package=jdk&version=21) and [**git**](https://git-scm.com/downloads) installed
* Open PowerShell (or Bash if you are using Linux)
* Navigate to the Directory you wish to copy the Sources to
```shell
cd $HOME/Downloads/
```
* Download the Sources
```shell
git clone https://github.com/fawnoculus/Vanilla-Backrooms
```
* enter the sources directory
```shell
cd Vanilla-Backrooms
```
* build the Mod
```shell
./gradlew build
```
If the Command Returns with saying **BUILD SUCCESSFUL** then you should be able to find the mod file at
**"Downloads/Vanilla-Backrooms/build/libs/Vanilla-Backrooms-***VERSION***.jar"**

## Licence
This software is licensed under the GNU Public License version 3.0 In short: This software is free, you may run the software freely, create modified versions,
distribute this software and distribute modified versions, as long as the modified software too has a free software license. The full license can be found in the `LICENSE.txt` file.
