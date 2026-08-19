package com.replaymod.core.utils;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;

/**
 * Restrictions set by the server,
 * @see <a href="https://gist.github.com/Johni0702/2547c463e51f65f312cb">Replay Restrictions Gist</a>
 */
public class Restrictions {
    public static final Identifier PLUGIN_CHANNEL = Identifier.of("replaymod", "restrict");

    public record Payload() implements CustomPayload {
        public static final Id<Payload> ID = new Id<>(PLUGIN_CHANNEL);
        public static final PacketCodec<RegistryByteBuf, Payload> CODEC = PacketCodec.unit(new Payload());

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    private boolean noXray;
    private boolean noNoclip;
    private boolean onlyFirstPerson;
    private boolean onlyRecordingPlayer;

    public String handle(CustomPayloadS2CPacket packet) {
        return null;
    }

    public boolean isNoXray() {
        return noXray;
    }

    public boolean isNoNoclip() {
        return noNoclip;
    }

    public boolean isOnlyFirstPerson() {
        return onlyFirstPerson;
    }

    public boolean isOnlyRecordingPlayer() {
        return onlyRecordingPlayer;
    }
}
