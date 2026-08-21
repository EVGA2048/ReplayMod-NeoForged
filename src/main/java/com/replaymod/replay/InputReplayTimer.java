package com.replaymod.replay;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.utils.WrappedTimer;
import com.replaymod.core.versions.MCVer;
import com.replaymod.replay.camera.CameraController;
import com.replaymod.replay.camera.CameraEntity;
import de.johni0702.minecraft.gui.versions.ScreenExt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

//#if MC>=11802
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
//#endif

//#if MC>=11400
import org.lwjgl.glfw.GLFW;
//#else
//$$ import net.minecraft.client.settings.KeyBinding;
//$$ import net.minecraftforge.client.ForgeHooksClient;
//$$ import org.lwjgl.input.Mouse;
//$$ import net.minecraftforge.fml.common.FMLCommonHandler;
//#if MC>=10800
//$$ import java.io.IOException;
//#else
//$$ import com.replaymod.replay.gui.screen.GuiOpeningReplay;
//$$ import net.minecraft.client.renderer.entity.RenderManager;
//#endif
//#endif

//#if MC>=10904
//#else
//$$ import net.minecraft.client.multiplayer.WorldClient;
//#endif

public class InputReplayTimer extends WrappedTimer {
    private final ReplayModReplay mod;
    private final MinecraftClient mc;
    
    public InputReplayTimer(RenderTickCounter wrapped, ReplayModReplay mod) {
        super(wrapped);
        this.mod = mod;
        this.mc = mod.getCore().getMinecraft();
    }

    @Override
    public int beginRenderTick(long sysClock, boolean tick) {
        int ticksThisFrame = super.beginRenderTick(sysClock, tick);

        ReplayMod.instance.runTasks();

        if (mod.getReplayHandler() != null) {
            if (mc.currentScreen instanceof DownloadingTerrainScreen) {
                mc.currentScreen.close();
            }
        }

        // If we are in a replay, we have to manually process key and mouse events as the
        // tick speed may vary or there may not be any ticks at all (when the replay is paused)
        if (mod.getReplayHandler() != null && mc.world != null && mc.player != null) {
            //#if MC>=11400
            if (mc.currentScreen == null || ((ScreenExt) mc.currentScreen).doesPassEvents()) {
                GLFW.glfwPollEvents();
                MCVer.processKeyBinds();
            }
            mc.keyboard.pollDebugCrash();
            //#endif
        }
        //#if MC>=11600
        return ticksThisFrame;
        //#endif
    }

    public static void handleScroll(int wheel) {
        if (wheel != 0) {
            ReplayHandler replayHandler = ReplayModReplay.instance.getReplayHandler();
            if (replayHandler != null) {
                CameraEntity cameraEntity = replayHandler.getCameraEntity();
                if (cameraEntity != null) {
                    CameraController controller = cameraEntity.getCameraController();
                    while (wheel > 0) {
                        controller.increaseSpeed();
                        wheel--;
                    }
                    while (wheel < 0) {
                        controller.decreaseSpeed();
                        wheel++;
                    }
                }
            }
        }
    }
}
