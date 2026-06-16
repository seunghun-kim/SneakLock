package org.teamck.sneaklock.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
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

            // Check left shift state
            boolean currentLeftShift = InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
            // Check right shift state
            boolean currentRightShift = InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT);

            // Check if both shifts are currently pressed
            boolean currentBothShifts = currentLeftShift && currentRightShift;

            // Toggle when both shifts are newly pressed
            if (currentBothShifts && !bothShiftsPressed) {
                sneakLocked = !sneakLocked;
                LOGGER.info("Sneak lock toggled: {}", sneakLocked ? "ON" : "OFF");
                // Send action bar message to player
                client.player.sendOverlayMessage(Component.literal("Sneak Lock: " + (sneakLocked ? "§aON" : "§cOFF")));
            }

            // Update previous state
            bothShiftsPressed = currentBothShifts;
        });
    }
}
