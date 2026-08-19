package com.replaymod.render.mixin;

import com.replaymod.render.capturer.CubicOpenGlFrameCapturer;
import com.replaymod.render.hooks.EntityRendererHandler;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.replaymod.core.versions.MCVer.getMinecraft;

@Mixin(value = net.minecraft.client.render.GameRenderer.class)
public abstract class Mixin_Omnidirectional_Rotation {
    private EntityRendererHandler getHandler() {
        return ((EntityRendererHandler.IEntityRenderer) getMinecraft().gameRenderer).replayModRender_getHandler();
    }

    @ModifyArg(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;setupFrustum(Lnet/minecraft/util/math/Vec3d;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"
            ),
            index = 1
    )
    private Matrix4f replayModRender_setupCubicFrameRotation(Matrix4f view) {
        if (getHandler() != null && getHandler().data instanceof CubicOpenGlFrameCapturer.Data) {
            CubicOpenGlFrameCapturer.Data data = (CubicOpenGlFrameCapturer.Data) getHandler().data;
            float angle = 0;
            float x = 0;
            float y = 0;
            switch (data) {
                case FRONT:
                    angle = 0;
                    y = 1;
                    break;
                case RIGHT:
                    angle = 90;
                    y = 1;
                    break;
                case BACK:
                    angle = 180;
                    y = 1;
                    break;
                case LEFT:
                    angle = -90;
                    y = 1;
                    break;
                case TOP:
                    angle = -90;
                    x = 1;
                    break;
                case BOTTOM:
                    angle = 90;
                    x = 1;
                    break;
            }
            view.rotate(new Quaternionf().fromAxisAngleDeg(new Vector3f(x, y, 0), angle));
            getMinecraft().worldRenderer.scheduleTerrainUpdate();
        }
        return view;
    }
}
