package de.johni0702.minecraft.gui.versions.mixin;

import de.johni0702.minecraft.gui.versions.callbacks.RenderHudCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class Mixin_RenderHudCallback {
    @Inject(method = "render", at = @At("RETURN"))
    private void renderOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        RenderHudCallback.EVENT.invoker().renderHud(context, tickCounter.getTickDelta(false));
    }
}
