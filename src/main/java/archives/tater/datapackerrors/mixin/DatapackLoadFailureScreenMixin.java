package archives.tater.datapackerrors.mixin;

import archives.tater.datapackerrors.DatapackErrorViewer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(DatapackLoadFailureScreen.class)
public abstract class DatapackLoadFailureScreenMixin extends Screen {

    protected DatapackLoadFailureScreenMixin(Component title) {
        super(title);
    }

    @ModifyArg(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"),
            index = 1
    )
    private int moveDownButtons(int y) {
        return height / 2 + 90;
    }

    @Inject(
            method = "init",
            at = @At("TAIL")
    )
    private void addErrorText(CallbackInfo ci) {
        var errorsWidget = DatapackErrorViewer.getErrorsWidget(this, font);
        if (errorsWidget == null) return;
        addRenderableWidget(errorsWidget);
    }

    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/MultiLineLabel;visitLines(Lnet/minecraft/client/gui/TextAlignment;IIILnet/minecraft/client/gui/ActiveTextCollector;)I"),
            index = 2
    )
    private int moveUpText(int y) {
        return height / 2 - 100;
    }
}