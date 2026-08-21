package com.replaymod.recording.handler;

import com.replaymod.core.ReplayMod;
import com.replaymod.core.utils.ModCompat;
import com.replaymod.core.utils.Utils;
import com.replaymod.editor.gui.MarkerProcessor;
import com.replaymod.recording.ServerInfoExt;
import com.replaymod.recording.Setting;
import com.replaymod.recording.gui.GuiRecordingControls;
import com.replaymod.recording.gui.GuiRecordingOverlay;
import com.replaymod.recording.mixin.NetworkManagerAccessor;
import com.replaymod.recording.packet.PacketListener;
import com.replaymod.replaystudio.replay.ReplayFile;
import com.replaymod.replaystudio.replay.ReplayMetaData;
import io.netty.channel.Channel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.network.ClientConnection;
import org.apache.logging.log4j.Logger;

//#if MC>=11900
import com.replaymod.recording.mixin.ClientLoginNetworkHandlerAccessor;
//#endif

//#if MC>=11600
import net.minecraft.world.World;
//#else
//#if MC>=11400
//$$ import net.minecraft.world.dimension.DimensionType;
//#endif
//$$
//#if MC>=10800
//$$ import net.minecraft.world.level.LevelGeneratorType;
//#endif
//#endif

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.replaymod.core.versions.MCVer.getMinecraft;

/**
 * Handles connection events and initiates recording if enabled.
 */
public class ConnectionEventHandler {

    private static final String DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    private static final MinecraftClient mc = getMinecraft();

    private final Logger logger;
    private final ReplayMod core;

    private RecordingEventHandler recordingEventHandler;
    private PacketListener packetListener;
    private GuiRecordingOverlay guiOverlay;
    private GuiRecordingControls guiControls;

    public ConnectionEventHandler(Logger logger, ReplayMod core) {
        this.logger = logger;
        this.core = core;
    }

    /**
     * @return true if recording started or was permanently skipped; false to retry later
     */
    public boolean onConnectedToServerEvent(ClientConnection networkManager) {
        try {
            boolean local = networkManager.isLocal();
            if (local) {
                var server = mc.getServer();
                var overworld = server == null ? null : server.getWorld(World.OVERWORLD);
                if (overworld != null && overworld.isDebugWorld()) {
                    logger.info("Debug World recording is not supported.");
                    return true;
                }
                if(!core.getSettingsRegistry().get(Setting.RECORD_SINGLEPLAYER)) {
                    logger.info("Singleplayer Recording is disabled");
                    return true;
                }
            } else {
                if(!core.getSettingsRegistry().get(Setting.RECORD_SERVER)) {
                    logger.info("Multiplayer Recording is disabled");
                    return true;
                }
            }

            ServerInfo serverInfo = null;
            if (networkManager.getPacketListener() instanceof ClientLoginNetworkHandlerAccessor loginNetworkHandler) {
                serverInfo = loginNetworkHandler.getServerInfo();
            }
            if (serverInfo == null) {
                serverInfo = mc.getCurrentServerEntry();
            }

            String worldName;
            String serverName = null;
            boolean autoStart = core.getSettingsRegistry().get(Setting.AUTO_START_RECORDING);
            if (local) {
                if (mc.getServer() == null) {
                    return false;
                }
                worldName = mc.getServer().getSaveProperties().getLevelName();
                //#else
                //$$ worldName = mc.getServer().getLevelName();
                //#endif
                serverName = worldName;
            //#if MC>=11100
            //#if MC>=12002
            } else if (serverInfo != null && serverInfo.isRealm()) {
            //#else
            //$$ } else if (mc.isConnectedToRealms()) {
            //#endif
                // we can't access the server name without tapping too deep in the Realms Library
                worldName = "A Realms Server";
            //#endif
            } else if (serverInfo != null) {
                worldName = serverInfo.address;
                if (!I18n.translate("selectServer.defaultName").equals(serverInfo.name)) {
                    serverName = serverInfo.name;
                }

                Boolean autoStartServer = ServerInfoExt.from(serverInfo).getAutoRecording();
                if (autoStartServer != null) {
                    autoStart = autoStartServer;
                }
            } else {
                logger.info("Recording not started as the world is neither local nor remote (probably too early).");
                return false;
            }

            if (ReplayMod.isMinimalMode()) {
                // Recording controls are not supported in minimal mode, so always auto-start
                autoStart = true;
            }

            String name = sdf.format(Calendar.getInstance().getTime());
            Path outputPath = Utils.replayNameToPath(core.folders.getRecordingFolder(), name);
            ReplayFile replayFile = core.files.open(outputPath);

            replayFile.writeModInfo(ModCompat.getInstalledNetworkMods());

            ReplayMetaData metaData = new ReplayMetaData();
            metaData.setSingleplayer(local);
            metaData.setServerName(worldName);
            metaData.setCustomServerName(serverName);
            metaData.setGenerator("ReplayMod v" + ReplayMod.instance.getVersion());
            metaData.setDate(System.currentTimeMillis());
            metaData.setMcVersion(ReplayMod.instance.getMinecraftVersion());
            metaData.setProtocolVersion(com.replaymod.core.versions.MCVer.getProtocolVersion());
            packetListener = new PacketListener(core, outputPath, replayFile, metaData);
            Channel channel = ((NetworkManagerAccessor) networkManager).getChannel();
            PacketListener.inject(channel, packetListener);

            recordingEventHandler = new RecordingEventHandler(packetListener);
            recordingEventHandler.register();

            guiControls = new GuiRecordingControls(core, packetListener, autoStart);
            guiControls.register();

            guiOverlay = new GuiRecordingOverlay(mc, core.getSettingsRegistry(), guiControls);
            guiOverlay.register();

            if (autoStart) {
                core.printInfoToChat("replaymod.chat.recordingstarted");
            } else {
                packetListener.addMarker(MarkerProcessor.MARKER_NAME_START_CUT, 0);
            }
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            core.printWarningToChat("replaymod.chat.recordingfailed");
            return false;
        }
    }

    public void reset() {
        if (packetListener != null) {
            guiControls.unregister();
            guiControls = null;
            guiOverlay.unregister();
            guiOverlay = null;
            recordingEventHandler.unregister();
            recordingEventHandler = null;
            packetListener = null;
        }
    }

    public PacketListener getPacketListener() {
        return packetListener;
    }
}
