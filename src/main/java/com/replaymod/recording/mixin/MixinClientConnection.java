package com.replaymod.recording.mixin;

import com.replaymod.recording.ReplayModRecording;
import com.replaymod.recording.packet.PacketListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class MixinClientConnection {
    @Shadow
    private Channel channel;

    @Inject(method = "setCompressionThreshold", at = @At("RETURN"))
    private void ensureReplayModRecorderIsAfterDecompress(CallbackInfo ci) {
        reinsertRecorder();
    }

    @Inject(method = "transitionInbound", at = @At("RETURN"))
    private void replayModKeepRecorderAfterProtocolSwitch(CallbackInfo ci) {
        reinsertRecorder();
    }

    private void reinsertRecorder() {
        if (channel == null) {
            return;
        }
        PacketListener listener = null;
        ChannelHandler existing = channel.pipeline().get(PacketListener.RAW_RECORDER_KEY);
        if (existing instanceof PacketListener packetListener) {
            listener = packetListener;
        } else if (ReplayModRecording.instance != null
                && ReplayModRecording.instance.getConnectionEventHandler() != null) {
            listener = ReplayModRecording.instance.getConnectionEventHandler().getPacketListener();
        }
        if (listener == null) {
            return;
        }
        PacketListener.inject(channel, listener);
    }
}
