package org.teamck.sneaklock.mixin.client;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.teamck.sneaklock.client.SneaklockClient;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    private void onIsPressed(CallbackInfoReturnable<Boolean> cir) {
        KeyBinding self = (KeyBinding)(Object)this;
        if (SneaklockClient.isSneakLocked() && self.getTranslationKey().equals("key.jump")) {
            cir.setReturnValue(false);
        }
    }
} 