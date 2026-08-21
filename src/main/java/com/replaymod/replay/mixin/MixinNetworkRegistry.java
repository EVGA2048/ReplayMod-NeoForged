package com.replaymod.replay.mixin;

import com.replaymod.replay.ReplayModReplay;
import net.minecraft.network.listener.ClientConfigurationPacketListener;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkRegistry.class)
public abstract class MixinNetworkRegistry {
    /**
     * 回放没有 NeoForge 握手包（或解不出来），客户端会走 vanilla fallback，
     * 然后被整合包里非 optional 的 payload 踢掉。
     */
    @Inject(
            method = "initializeOtherConnection(Lnet/minecraft/network/listener/ClientConfigurationPacketListener;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void replaymod$skipVanillaNeoForgeCheck(ClientConfigurationPacketListener listener, CallbackInfo ci) {
        if (ReplayModReplay.isReplayConnection(listener.getConnection())) {
            NetworkRegistry.configureMockConnection(listener.getConnection());
            ci.cancel();
        }
    }
}
