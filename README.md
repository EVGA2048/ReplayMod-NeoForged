# ReplayMod-NeoForged

[Replay Mod](https://www.replaymod.com/) 的 NeoForge 移植：录像、回放、镜头路径。给 NeoForge 整合包用，不必再靠 Connector 去跑官方 Fabric 版。

本仓库由 [ReForgedPlay](https://github.com/ferriarnus/ReForgedPlay)（1.20.4）往上接，**目前只做 Minecraft 1.21.1**。其它版本没有在做，也请不要默认能装上。

这不是官方 Replay Mod。有问题请在 **本仓库** 开 Issue，不要去官方 GitHub / Discord 问。

## 现在能用的版本

| 游戏 | 加载器 | 模组版本 | 状态 |
|------|--------|----------|------|
| **1.21.1** | NeoForge 21.1.x | 0.4-1.21.1 | 开发中，实验性 |
| 1.20.4 | NeoForge | — | 请用上游 [ReForgedPlay](https://github.com/ferriarnus/ReForgedPlay) |
| 其它 | — | — | 没有 |

开发在 [`1.21.1`](https://github.com/EVGA2048/ReplayMod-NeoForged/tree/1.21.1) 分支。安装请下 [Release](https://github.com/EVGA2048/ReplayMod-NeoForged/releases) 里的 jar，不要自己捡 `dev-shadow`。

需要 **Java 21**。进游戏后默认 **R** 开始/停止录像，回放从主菜单的 Replay Viewer 进。

## 安装

1. 实例必须是 **NeoForge 1.21.1**。
2. 把 `ReplayMod-NeoForged-0.4-1.21.1.jar` 放进 `mods`。
3. **不要**再装一份官方 Fabric ReplayMod。

如果整合包带了 Sinytra Connector：只留本模组这一份 jar，删掉 `replaymod-*-fabric*.jar`，并清掉 `mods/.connector/` 里映射出来的 `replaymod-*_mapped_*.jar`。两份都会注册 `replaymod`，一起装会抢加载，甚至开局就崩。

## 还不稳定的地方

录像和回放优先。下面这些还没跟上或没怎么测：Iris 的 ODS、部分键鼠 / 天空 mixin、360° 和立体导出、路径预览。和 Sodium、Iris 等渲染模组叠在一起，请自备日志。

## 自己构建

JDK 21，然后：

```bash
git clone -b 1.21.1 https://github.com/EVGA2048/ReplayMod-NeoForged.git
cd ReplayMod-NeoForged
./gradlew remapJar
```

Windows 用 `gradlew.bat`。产物在 `build/libs/ReplayMod-NeoForged-0.4-1.21.1.jar`。

## 致谢

Replay Mod 是 [CrushedPixel](https://github.com/CrushedPixel) 和 [johni0702](https://github.com/johni0702) 的作品。NeoForge 1.20.4 移植来自 [Ferri_Arnus](https://github.com/ferriarnus)。Kickstarter 支持者见 `src/main/resources/ThankYou.txt`。

许可：[GPL-3.0-or-later](LICENSE.md)。无保修。

---

**English** — Unofficial Replay Mod port for NeoForge. **1.21.1 only** right now (version `0.4-1.21.1`). Not official; please do not ask the Replay Mod authors for help with this fork. Install the Release jar (not `dev-shadow`), and do not also install Fabric ReplayMod or a Connector-mapped copy. Build: `./gradlew remapJar` on branch `1.21.1`, JDK 21. GPL-3.0-or-later.
