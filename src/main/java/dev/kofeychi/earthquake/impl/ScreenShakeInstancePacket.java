package dev.kofeychi.earthquake.impl;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record ScreenShakeInstancePacket(String encoded) implements CustomPayload {
    public static final CustomPayload.Id<ScreenShakeInstancePacket> ID = new CustomPayload.Id<>(Earthquake.of("screenshakepacket"));
    public static final PacketCodec<RegistryByteBuf, ScreenShakeInstancePacket> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ScreenShakeInstancePacket::encoded
            , ScreenShakeInstancePacket::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
