package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayModReplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ReconfiguringScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReconfiguringScreen.class)
public abstract class MixinReconfiguringScreen {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void replaymod$closeDuringReplay(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (ReplayModReplay.instance == null || ReplayModReplay.instance.getReplayHandler() == null
                || client.world == null) {
            return;
        }
        client.setScreen(null);
        ci.cancel();
    }
}
