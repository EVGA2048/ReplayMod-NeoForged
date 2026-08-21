package com.replaymod.recording.mixin;

import com.replaymod.recording.ReplayModRecording;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConfigurationNetworkHandler.class)
public abstract class MixinNetHandlerConfigClient {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void replayModInitiateRecording(CallbackInfo ci) {
        if (ReplayModRecording.instance != null) {
            ReplayModRecording.instance.initiateRecording(
                    ((ClientConfigurationNetworkHandler) (Object) this).getConnection());
        }
    }
}
