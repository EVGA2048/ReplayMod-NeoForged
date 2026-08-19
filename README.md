# ReplayMod-NeoForged

把 [Replay Mod](https://www.replaymod.com/) 接到 **NeoForge 1.21.1** 的非官方移植。

可以录像、回放、做简单的镜头路径。目标是让 NeoForge 整合包不用再靠 Sinytra Connector 去跑官方 Fabric 版。

> **这不是官方 Replay Mod。**  
> 请不要到 [ReplayMod/ReplayMod](https://github.com/ReplayMod/ReplayMod) 或官方 Discord 问这个 fork 的问题。那边帮不了，也容易给他们添麻烦。

当前版本：**0.4-1.21.1**（实验性，能进游戏不等于功能都齐）。

## 兼容性

| 项目 | 版本 |
|------|------|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.x（构建时用 21.1.217） |
| Java | 21 |
| Loader | 原生 NeoForge（`neoforge.mods.toml`） |

不支持 Fabric / Quilt 直接加载。也不要和官方 Fabric ReplayMod 混装。

## 安装

1. 从 [Releases](https://github.com/EVGA2048/ReplayMod-NeoForged/releases) 下载 `ReplayMod-NeoForged-0.4-1.21.1.jar`。  
   不要用带 `dev-shadow` 的 jar，那是构建中间产物。
2. 放进实例的 `mods` 文件夹。
3. 启动器选 **NeoForge 1.21.1**。
4. 进游戏后，默认用 **R** 开始/停止录像（可在控制里改）。回放从主菜单的 Replay Viewer 进。

### 整合包 / Connector

如果你的整合包装了 [Sinytra Connector](https://github.com/Sinytra/Connector)：

- **只留这一份** ReplayMod-NeoForged jar。
- 删掉官方 `replaymod-*-fabric*.jar`。
- 清掉 `mods/.connector/` 里 Connector 映射出来的 `replaymod-*_mapped_*.jar`。

两边的 `modId` 都是 `replaymod`。FML 和 Connector 会抢同一份模组，轻则跳过、重则加载 overlay 直接崩。

## 和上游的关系

```
ReplayMod（Fabric，官方）
    └── ferriarnus/ReForgedPlay（NeoForge 1.20.4）
            └── 本仓库（NeoForge 1.21.1）
```

- 官方：[ReplayMod/ReplayMod](https://github.com/ReplayMod/ReplayMod)
- 1.20.4 NeoForge 底：[ferriarnus/ReForgedPlay](https://github.com/ferriarnus/ReForgedPlay)
- 本 fork：Yarn 映射 + Architectury Loom，清单按 NeoForge 1.21 来，不走 Fabric 元数据

游戏内显示名是 **ReplayMod-NeoForged**。jar 里仍注册 `replaymod`（以及一个 `reforgedplaymod` stub），方便其它模组按原 id 探测。

## 已知限制

移植还在进行，下面这些请先当「没有」或「可能炸」：

- **Iris ODS / 部分光影挂钩**还没接到 1.21.1，相关源码构建时是排除的。
- Mouse / Keyboard / `renderSky` 仍有 mixin remap 警告，键鼠或天空相关功能可能不稳定。
- 360° / 立体导出刚按新的 `Matrix4f` 视图矩阵改过，还没充分实测。
- 路径预览在 1.21 的渲染管线上可能对不齐。
- 没有对所有 NeoForge 整合包做兼容测试。Sodium / Iris / 其它渲染模组出问题请带最新日志来。

录像和回放是优先保证的路径。导出、光影、花活其次。

## 从源码构建

需要 **JDK 21**。仓库自带 Gradle 9.2.1 wrapper。

```bash
git clone -b 1.21.1 https://github.com/EVGA2048/ReplayMod-NeoForged.git
cd ReplayMod-NeoForged
./gradlew remapJar
```

产物：

```
build/libs/ReplayMod-NeoForged-0.4-1.21.1.jar
```

Windows 用 `gradlew.bat remapJar`。第一次会拉 Yarn / NeoForge，需要能访问 Maven 和 Minecraft 库。

## 反馈

欢迎开 [Issue](https://github.com/EVGA2048/ReplayMod-NeoForged/issues)，最好带上：

- Minecraft / NeoForge 精确版本
- 完整 `crash-reports` 或 `latest.log`（不要只截最后几行）
- `mods` 列表里有没有另一份 ReplayMod、有没有 Connector

功能请求也可以提，但请理解这是业余移植，没有官方排期。

## 致谢

- [CrushedPixel](https://github.com/CrushedPixel) 与 [johni0702](https://github.com/johni0702) 做了 Replay Mod 本身
- [Ferri_Arnus](https://github.com/ferriarnus) 的 ReForgedPlay 是 1.20.4 NeoForge 的起点
- 所有在 Kickstarter 上支持过 Replay Mod 的人（见 `src/main/resources/ThankYou.txt`）

## 许可

[GPL-3.0-or-later](LICENSE.md)。Replay Mod 是自由软件；本移植同样按 GPL 分发。没有保修。

---

## English

Unofficial [Replay Mod](https://www.replaymod.com/) port for **NeoForge 1.21.1**, based on [ferriarnus/ReForgedPlay](https://github.com/ferriarnus/ReForgedPlay).

This is **not** the official Replay Mod. Please do not ask for support on the official GitHub or Discord.

- Minecraft 1.21.1, NeoForge 21.1.x, Java 21
- Install the `ReplayMod-NeoForged-*.jar` from [Releases](https://github.com/EVGA2048/ReplayMod-NeoForged/releases) (not `dev-shadow`)
- Do **not** also install Fabric ReplayMod, and do not let Sinytra Connector remap a second copy
- Build with `./gradlew remapJar` (JDK 21). Output is `build/libs/ReplayMod-NeoForged-0.4-1.21.1.jar`
- Experimental. Recording/replay first; Iris ODS, some input mixins, and fancy exporters are incomplete

License: GPL-3.0-or-later.
