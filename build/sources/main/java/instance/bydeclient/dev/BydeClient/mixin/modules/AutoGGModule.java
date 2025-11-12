package instance.bydeclient.dev.BydeClient.mixin.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;

/**
 * Мод AutoGG - автоматически пишет "gg" после окончания игры
 */
public class AutoGGModule extends Module {
    private long lastMessageTime = 0;
    private static final long MESSAGE_DELAY = 1000; // 1 секунда задержка между сообщениями
    private boolean gameEnded = false;

    public AutoGGModule() {
        super("AutoGG", "Автоматически пишет 'gg' после игры");
    }

    @Override
    public void onEnable() {
        System.out.println("[BydeClient] AutoGG мод включен");
        gameEnded = false;
    }

    @Override
    public void onDisable() {
        System.out.println("[BydeClient] AutoGG мод отключен");
    }

    @Override
    public void onUpdate() {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        // Проверяем сообщения в чате на признаки конца игры
        checkForGameEnd(mc.thePlayer);
    }

    private void checkForGameEnd(EntityPlayerSP player) {
        long currentTime = System.currentTimeMillis();

        // Проверяем, прошло ли достаточно времени с последнего сообщения
        if (currentTime - lastMessageTime < MESSAGE_DELAY) {
            return;
        }

        // Проверяем различные сообщения об окончании игры
        String[] endGameMessages = {
            "You died",
            "Game Over",
            "Victory",
            "Defeat",
            "You won",
            "You lost",
            "Match ended"
        };

        // Если игра закончилась, отправляем "gg"
        if (gameEnded) {
            sendMessage(player, "gg");
            gameEnded = false;
            lastMessageTime = currentTime;
        }
    }

    public void onGameEnd() {
        gameEnded = true;
    }

    private void sendMessage(EntityPlayerSP player, String message) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(message);
            System.out.println("[BydeClient] AutoGG: Отправлено сообщение: " + message);
        }
    }

    /**
     * Вызывается когда игрок умирает
     */
    public void onPlayerDeath() {
        gameEnded = true;
    }

    /**
     * Вызывается когда игра заканчивается
     */
    public void onMatchEnd() {
        gameEnded = true;
    }
}
