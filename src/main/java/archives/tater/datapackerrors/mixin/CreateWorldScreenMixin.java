package archives.tater.datapackerrors.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.jetbrains.annotations.Nullable;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @ModifyArg(
            method = "method_49629",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/ConfirmScreen;<init>(Lit/unimi/dsi/fastutil/booleans/BooleanConsumer;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)V"),
            index = 0
    )
    private BooleanConsumer getConsumer(BooleanConsumer callback, @Share("callback") LocalRef<BooleanConsumer> callbackRef) {
        callbackRef.set(callback);
        return callback;
    }

    @ModifyArg(
            method = "method_49629",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", ordinal = 0)
    )
    private Screen replaceScreen(@Nullable Screen guiScreen, @Share("callback") LocalRef<BooleanConsumer> callbackRef) {
        var callback = callbackRef.get();
        return new DatapackLoadFailureScreen(() -> callback.accept(true), () -> callback.accept(false));
    }
}
