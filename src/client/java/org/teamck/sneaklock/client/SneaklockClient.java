package org.teamck.sneaklock.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SneaklockClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("sneaklock");
    private static boolean sneakLocked = false;
    private static boolean bothShiftsPressed = false;

    public static boolean isSneakLocked() {
        return sneakLocked;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("SneakLock initialized");
        
        // Register the client tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            ClientPlayerEntity player = client.player;
            GameOptions options = client.options;

            // Check left shift state
            boolean currentLeftShift = InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT);
            // Check right shift state
            boolean currentRightShift = InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);
            
            // Check if both shifts are currently pressed
            boolean currentBothShifts = currentLeftShift && currentRightShift;

            // Toggle when both shifts are newly pressed
            if (currentBothShifts && !bothShiftsPressed) {
                sneakLocked = !sneakLocked;
                LOGGER.info("Sneak lock toggled: {}", sneakLocked ? "ON" : "OFF");
                // Send action bar message to player
                player.sendMessage(Text.literal("Sneak Lock: " + (sneakLocked ? "§aON" : "§cOFF")), true);
            }

            // Update previous state
            bothShiftsPressed = currentBothShifts;

            // Handle sneak state
            if (sneakLocked) {
                // Force sneak input state
                options.sneakKey.setPressed(true);
                player.setSneaking(true);
            } else if (!currentLeftShift && !currentRightShift) {
                // Only release sneak if neither shift is pressed
                options.sneakKey.setPressed(false);
                player.setSneaking(false);
            }
        });
    }
}
