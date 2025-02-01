package dev.kofeychi.earthquake.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.kofeychi.earthquake.impl.Debug.RegDebug;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class Earthquake implements ModInitializer , ClientModInitializer {
    public static final Gson GSON = new GsonBuilder().setLenient().create();
    public static MinecraftServer Server;
    public static Identifier of(String ID){
        return Identifier.of("earthquake",ID);
    }
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(ScreenShakeInstancePacket.ID, ScreenShakeInstancePacket.CODEC);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> this.Server = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> this.Server = null);
        if (FabricLoader.getInstance().isDevelopmentEnvironment()){
            RegDebug.init();
        }
    }

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ScreenShakeInstancePacket.ID, (payload, context) -> {
            context.client().execute(() -> {
                        String data = payload.encoded();
                        ScreenShakeInstance ssi = GSON.fromJson(data, ScreenShakeInstance.class);
                        ScreenShakeHandler.addInstance(ssi);
            });
        });
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!SharedValues.isFrozen|client.isWindowFocused()) {
                ScreenShakeHandler.Tick();
            }
        });
        /*HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
                int a = 8;
                String data = "";
                int i = 0;
                for(Iterator<ScreenShakeInstance> var2 = Instances.iterator(); var2.hasNext(); i++) {
                    ScreenShakeInstance instance = (ScreenShakeInstance) var2.next();
                    data += "ID: "+i+" Data: "+instance.toString();
                }
                int maxLenght = 120;
                Pattern p = Pattern.compile("\\G\\s*(.{1," + maxLenght + "})(?=\\s|$)", Pattern.DOTALL);
                Matcher m = p.matcher(data);
                int b = 0;
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Instances: ", a, a * 1, 0xFFFFFFFF, false);
                while (m.find()) {
                    b += a;
                    drawContext.drawText(MinecraftClient.getInstance().textRenderer, m.group(1), a, a * 2 + b, 0xFFFFFFFF, false);
                }
                b += a * 2;
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Rot: " + ScreenShakeHandler.Rot.toString(), a, a * 2 + b, 0xFFFFFFFF, false);
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Pos: " + ScreenShakeHandler.Pos.toString(), a, a * 3 + b, 0xFFFFFFFF, false);
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "IRot: " + ScreenShakeHandler.IRot.toString(), a, a * 4 + b, 0xFFFFFFFF, false);
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "IPos: " + ScreenShakeHandler.IPos.toString(), a, a * 5 + b, 0xFFFFFFFF, false);
                drawContext.drawText(MinecraftClient.getInstance().textRenderer, "Instances amount: " + (Instances.size()), a, a * 6 + b, 0xFFFFFFFF, false);
        });
         */
    }
}
