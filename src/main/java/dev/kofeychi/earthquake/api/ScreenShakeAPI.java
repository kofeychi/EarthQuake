package dev.kofeychi.earthquake.api;

import dev.kofeychi.earthquake.impl.Earthquake;
import dev.kofeychi.earthquake.impl.ScreenShakeInstance;
import dev.kofeychi.earthquake.impl.ScreenShakeInstancePacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class ScreenShakeAPI {
    public static void SendToAllPlayers(ScreenShakeInstance Instance){
        Earthquake.Server.getPlayerManager().getPlayerList().forEach(spe -> {
            ServerPlayNetworking.send(
                    spe,new ScreenShakeInstancePacket(InstanceToJson(Instance))
            );
        });
    }
    public static void SendToSpecificPlayers(List<ServerPlayerEntity> Targets, ScreenShakeInstance Instance){
        Targets.forEach(spe -> {
            ServerPlayNetworking.send(
                    spe,new ScreenShakeInstancePacket(InstanceToJson(Instance))
            );
        });
    }
    public static String InstanceToJson(ScreenShakeInstance InstanceToGson){
        return Earthquake.GSON.toJson(InstanceToGson);
    }
}
