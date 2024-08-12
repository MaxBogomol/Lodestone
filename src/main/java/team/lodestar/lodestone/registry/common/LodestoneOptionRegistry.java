package team.lodestar.lodestone.registry.common;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class LodestoneOptionRegistry {

    public static final OptionInstance<Double> SCREENSHAKE_INTENSITY = createSliderOption("lodestone.options.screenshakeIntensity");
    public static final OptionInstance<Double> FIRE_OFFSET = createSliderOption("lodestone.options.fireOffset");

    private static OptionInstance<Double> createSliderOption(String key) {
        return new OptionInstance<>(key,
                OptionInstance.cachedConstantTooltip(Component.translatable(key + ".tooltip")),
                (component, val) -> val == 0.0D ? Component.translatable("options.generic_value", component,
                        CommonComponents.OPTION_OFF) :
                        Component.translatable("options.percent_value", component, (int) (val * 100.0D)),
                OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt),
                Codec.doubleRange(0.0D, 1.0D), 1.0D, (val) -> {
        });
    }



    public static void addOption() {

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof AccessibilityOptionsScreen accessibilityOptionsScreen) {
                accessibilityOptionsScreen.list.addSmall(SCREENSHAKE_INTENSITY, FIRE_OFFSET);
            }
        });


    }
}
