package org.teamck.sneaklock.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamck.sneaklock.client.SneaklockClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mixin(KeyMapping.class)
public class KeyBindingMixin {
    @Shadow @Final private String name;

    private static final Logger LOGGER = LoggerFactory.getLogger("sneaklock");
    private boolean wasPressed = false;

    @Inject(method = "isDown", at = @At("HEAD"), cancellable = true)
    private void onIsPressed(CallbackInfoReturnable<Boolean> cir) {
        KeyMapping self = (KeyMapping)(Object)this;
        String key = this.name;

        if (SneaklockClient.isSneakLocked()) {
            // Handle jump key
            if (key.equals("key.jump")) {
                boolean currentlyPressed = self.consumeClick();
                if (currentlyPressed && !wasPressed) {
                    LOGGER.info("Jump key pressed while sneak lock is active");
                    Minecraft client = Minecraft.getInstance();
                    if (client.player != null) {
                        client.player.sendOverlayMessage(Component.literal("§cJump cancelled - Sneak Lock active"));
                    }
                }
                wasPressed = currentlyPressed;
                cir.setReturnValue(false);
            }
            // Handle sneak key - force it to be pressed when locked
            else if (key.equals("key.sneak")) {
                cir.setReturnValue(true);
            }
        }
    }
}
