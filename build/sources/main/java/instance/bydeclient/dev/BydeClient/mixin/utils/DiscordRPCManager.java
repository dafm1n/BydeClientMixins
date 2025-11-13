package instance.bydeclient.dev.BydeClient.mixin.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class DiscordRPCManager {
    private static boolean initialized = false;
    private static long startTime = System.currentTimeMillis() / 1000;

    public static void init() {
        if (initialized) return;
        initialized = true;
        // logging removed
    }

    public static void update() {
        if (!initialized) return;

        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null) return;

            String state = "";
            String details = "";

            if (mc.thePlayer == null) {
                state = "В главном меню";
                details = "Выбор сервера";
            } else if (mc.thePlayer.worldObj == null) {
                state = "Загрузка мира...";
                details = "BydeClient";
            } else {
                ServerData serverData = mc.getCurrentServerData();

                if (serverData != null && serverData.serverIP != null) {
                    String serverName = serverData.serverName != null ? serverData.serverName : "Сервер";
                    String serverIP = serverData.serverIP;

                    if (serverIP.toLowerCase().contains("hypixel")) {
                        state = "Играет на Hypixel";
                        details = serverName;
                    } else {
                        state = "Играет на сервере";
                        details = serverName + " (" + serverIP + ")";
                    }
                } else {
                    state = "Одиночная игра";
                    details = "Выживание";
                }
            }

            // logging removed
        } catch (Exception e) {
            // logging removed
        }
    }

    public static void shutdown() {
        if (initialized) {
            initialized = false;
            // logging removed
        }
    }
}
