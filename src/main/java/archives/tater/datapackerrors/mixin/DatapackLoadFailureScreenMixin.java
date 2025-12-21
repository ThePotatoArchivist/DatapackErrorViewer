package archives.tater.datapackerrors.mixin;

import archives.tater.datapackerrors.DatapackErrorViewer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import static archives.tater.datapackerrors.DatapackErrorViewer.iterateUntilRepeat;

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
        var errors = DatapackErrorViewer.errors;
        if (errors == null) return;
        this.addRenderableWidget(new FittingMultiLineTextWidget(width / 2 - 180,
                height / 2 - 60,
                360,
                140,
                ComponentUtils.formatList(errors.entrySet().stream()
                        .map(entry ->
                                Component.literal(entry.getKey().registry() + "/" + entry.getKey().location() + "\n")
                                        .withStyle(ChatFormatting.WHITE)
                                        .append(
                                                ComponentUtils.formatList(
                                                        iterateUntilRepeat(entry.getValue(), Throwable::getCause)
                                                                .map(throwable -> Component.literal(throwable.getMessage())
                                                                        .withStyle(ChatFormatting.GRAY))
                                                                .toList(),
                                                        Component.literal("\n")
                                                )
                                        ))
                        .toList(),
                        Component.literal("\n\n")),
                font
        ));
    }

    @ModifyArg(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/MultiLineLabel;renderCentered(Lnet/minecraft/client/gui/GuiGraphics;II)V"),
            index = 2
    )
    private int moveUpText(int y) {
        return height / 2 - 100;
    }
}