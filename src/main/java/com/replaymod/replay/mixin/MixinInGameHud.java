//#if MC>=11400
package com.replaymod.replay.mixin;

import com.replaymod.replay.events.RenderHotbarCallback;
import com.replaymod.replay.events.RenderSpectatorCrosshairCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(method = "shouldRenderSpectatorCrosshair", at = @At("HEAD"), cancellable = true)
    private void shouldRenderSpectatorCrosshair(CallbackInfoReturnable<Boolean> ci) {
        Boolean state = RenderSpectatorCrosshairCallback.EVENT.invoker().shouldRenderSpectatorCrosshair();
        if (state != null) {
            ci.setReturnValue(state);
        }
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void shouldRenderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        Boolean state = RenderHotbarCallback.EVENT.invoker().shouldRenderHotbar();
        if (state == Boolean.FALSE) {
            ci.cancel();
        }
    }
}
//#endif
