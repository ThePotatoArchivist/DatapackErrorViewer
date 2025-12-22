package archives.tater.datapackerrors.mixin;

import archives.tater.datapackerrors.DatapackErrorViewer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;

import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Inject(
            method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Lnet/minecraft/core/RegistryAccess;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At("HEAD")
    )
    private static void clearErrors(CallbackInfoReturnable<RegistryAccess.Frozen> cir) {
        DatapackErrorViewer.errors = null;
    }

    @ModifyArg(
            method = "load(Lnet/minecraft/resources/RegistryDataLoader$LoadingFunction;Lnet/minecraft/core/RegistryAccess;Ljava/util/List;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/RegistryDataLoader;logErrors(Ljava/util/Map;)V"),
            index = 0
    )
    private static Map<ResourceKey<?>, Exception> getErrors(Map<ResourceKey<?>, Exception> errors) {
        DatapackErrorViewer.errors = errors;
        return errors;
    }
}
