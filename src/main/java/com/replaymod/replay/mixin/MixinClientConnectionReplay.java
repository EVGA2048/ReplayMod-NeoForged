package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayModReplay;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class MixinClientConnectionReplay {
    /**
     * DecoderException 会走 exceptionCaught，文案是「此服务器发送了一个无效的数据包」。
     */
    @Inject(method = "exceptionCaught", at = @At("HEAD"), cancellable = true)
    private void replaymod$swallowReplayExceptions(ChannelHandlerContext context, Throwable cause, CallbackInfo ci) {
        if (ReplayModReplay.isReplayConnection((ClientConnection) (Object) this)) {
            ReplayModReplay.LOGGER.warn("Ignoring connection exception during replay: {}", cause.toString());
            ci.cancel();
        }
    }
}
