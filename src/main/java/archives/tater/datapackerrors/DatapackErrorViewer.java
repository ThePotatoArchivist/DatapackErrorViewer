package archives.tater.datapackerrors;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

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

    public static FittingMultiLineTextWidget getErrorsWidget(Screen screen, Font font) {
        if (errors == null) return null;

        return new FittingMultiLineTextWidget(screen.width / 2 - 180,
                screen.height / 2 - 60,
                360,
                140,
                ComponentUtils.formatList(errors.entrySet().stream()
                        .map(entry ->
                                Component.literal(entry.getKey().registry() + "/" + entry.getKey().identifier() + "\n")
                                        .withStyle(ChatFormatting.WHITE)
                                        .append(ComponentUtils.formatList(
                                                iterateUntilRepeat(entry.getValue(), Throwable::getCause)
                                                        .map(throwable -> Component.literal(throwable.getMessage())
                                                                .withStyle(ChatFormatting.GRAY))
                                                        .toList(),
                                                Component.literal("\n")
                                        ))
                        ).toList(),
                        Component.literal("\n\n")),
                font
        );
    }

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            ResourceLoader.registerBuiltinPack(
                    Identifier.fromNamespaceAndPath(MOD_ID, "testerror"),
                    FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(),
                    PackActivationType.NORMAL
            );
	}
}