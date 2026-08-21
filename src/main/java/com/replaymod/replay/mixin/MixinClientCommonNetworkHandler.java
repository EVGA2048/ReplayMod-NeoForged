package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayModReplay;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class MixinClientCommonNetworkHandler {
    /**
     * 1.21 把包处理异常当成协议错误直接踢人。回放时 player 还没建好就会炸。
     */
    @Inject(method = "onPacketException", at = @At("HEAD"), cancellable = true)
    private void replaymod$ignorePacketErrors(Packet<?> packet, Exception exception, CallbackInfo ci) {
        if (ReplayModReplay.instance == null || ReplayModReplay.instance.getReplayHandler() == null) {
            return;
        }
        ReplayModReplay.LOGGER.debug("Ignoring packet error during replay: {}", packet.getClass().getName());
        ci.cancel();
    }
}
