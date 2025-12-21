package archives.tater.datapackerrors;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DatapackErrorViewer implements ModInitializer {
	public static final String MOD_ID = "datapackerrors";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static @Nullable Map<ResourceKey<?>, Exception> errors = null;

    public static <T> Stream<T> iterateUntilRepeat(T initial, UnaryOperator<T> next) {
        return Stream.iterate(initial, value -> value != null && next.apply(value) != value, next);
    }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            ResourceManagerHelper.registerBuiltinResourcePack(
                    ResourceLocation.fromNamespaceAndPath(MOD_ID, "testerror"),
                    FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                    ResourcePackActivationType.NORMAL
            );
	}
}