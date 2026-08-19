package com.replaymod.render.mixin;

import com.replaymod.render.hooks.EntityRendererHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=11400
@Mixin(InGameHud.class)
//#else
//$$ @Mixin({ GuiIngame.class, net.minecraftforge.client.GuiIngameForge.class })
//#endif
public abstract class Mixin_SkipHudDuringRender {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void replayModRender_skipHudDuringRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (((EntityRendererHandler.IEntityRenderer) MinecraftClient.getInstance().gameRenderer).replayModRender_getHandler() != null) {
            ci.cancel();
        }
    }
}
