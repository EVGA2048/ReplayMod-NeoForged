# ReplayMod-NeoForged

> **当前版本还不能用。** 不要下载、不要装进整合包当成品。1.21.1 的录像/回放都没跑通，开回放会停在加载界面。仓库只是开发中途的快照。

[Replay Mod](https://www.replaymod.com/) 的 NeoForge 移植：录像、回放、镜头路径。给 NeoForge 整合包用，不必再靠 Connector 去跑官方 Fabric 版。

本仓库由 [ReForgedPlay](https://github.com/ferriarnus/ReForgedPlay)（1.20.4）往上接，**目前只做 Minecraft 1.21.1**。其它版本没有在做。

这不是官方 Replay Mod。有问题请在 **本仓库** 开 Issue，不要去官方 GitHub / Discord 问。

## 版本

| 游戏 | 加载器 | 模组版本 | 状态 |
|------|--------|----------|------|
| **1.21.1** | NeoForge 21.1.x | 0.4-1.21.1 | **还不能用** |
| 1.20.4 | NeoForge | — | 请用上游 [ReForgedPlay](https://github.com/ferriarnus/ReForgedPlay) |
| 其它 | — | — | 没有 |

开发在 [`1.21.1`](https://github.com/EVGA2048/ReplayMod-NeoForged/tree/1.21.1) 分支。能用之前不会发可用 Release。

## 卡在哪

1.20.5+ 进世界前会走 CONFIGURATION。旧录制器把 handler 插在拆包器前面，`.mcpr` 里 LoginSuccess 之后存的是 **TCP 碎片** 而不是完整包。回放拼不出 Ready / GameJoin，一直停在加载界面。

分支里已经把**新录像**的注入点改到 `decompress` 之后，协议切换会重新 inject。旧录像的抢救先搁了。**新录能不能正常播，还没验证。** 在那之前当整模不能用。

其它没跟上的：Iris ODS、部分键鼠 / 天空 mixin、360° 和立体导出、路径预览。

## 自己构建（开发用）

JDK 21：

```bash
git clone -b 1.21.1 https://github.com/EVGA2048/ReplayMod-NeoForged.git
cd ReplayMod-NeoForged
./gradlew remapJar
```

产物在 `build/libs/ReplayMod-NeoForged-0.4-1.21.1.jar`。和官方 Fabric ReplayMod、Connector 映射出来的 `replaymod-*` 不能一起装。

## 致谢

Replay Mod 是 [CrushedPixel](https://github.com/CrushedPixel) 和 [johni0702](https://github.com/johni0702) 的作品。NeoForge 1.20.4 移植来自 [Ferri_Arnus](https://github.com/ferriarnus)。Kickstarter 支持者见 `src/main/resources/ThankYou.txt`。

许可：[GPL-3.0-or-later](LICENSE.md)。无保修。

---

**English** — Unofficial Replay Mod port for NeoForge, **1.21.1 only** (`0.4-1.21.1`). **Not usable yet.** Playback sticks on the loading screen (1.20.5+ CONFIG handshake; old recordings stored TCP fragments). Do not install this in a pack. Not official; do not ask the Replay Mod authors for help. GPL-3.0-or-later.
