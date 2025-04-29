package org.teamck.sneaklock.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamck.sneaklock.client.SneaklockClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("sneaklock");
    private boolean wasPressed = false;

    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    private void onIsPressed(CallbackInfoReturnable<Boolean> cir) {
        KeyBinding self = (KeyBinding)(Object)this;
        String key = self.getTranslationKey();
        
        if (SneaklockClient.isSneakLocked()) {
            // Handle jump key
            if (key.equals("key.jump")) {
                boolean currentlyPressed = self.wasPressed();
                if (currentlyPressed && !wasPressed) {
                    LOGGER.info("Jump key pressed while sneak lock is active");
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§cJump cancelled - Sneak Lock active"), true);
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