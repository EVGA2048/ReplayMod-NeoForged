package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayHandler;
import com.replaymod.replay.ReplayModReplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.io.IOException;

@Mixin(DownloadingTerrainScreen.class)
public abstract class Mixin_DownloadingTerrainScreen extends Screen {
    protected Mixin_DownloadingTerrainScreen(Text title) {
        super(title);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return ReplayModReplay.instance != null && ReplayModReplay.instance.getReplayHandler() != null
                || super.shouldCloseOnEsc();
    }

    @Override
    public void close() {
        MinecraftClient client = MinecraftClient.getInstance();
        ReplayHandler handler = ReplayModReplay.instance != null ? ReplayModReplay.instance.getReplayHandler() : null;
        if (handler != null && client.world == null) {
            client.execute(() -> {
                try {
                    ReplayHandler current = ReplayModReplay.instance.getReplayHandler();
                    if (current != null) {
                        current.endReplay();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        super.close();
    }
}
