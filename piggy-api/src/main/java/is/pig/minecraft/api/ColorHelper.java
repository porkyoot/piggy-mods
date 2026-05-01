package is.pig.minecraft.api;

import java.util.function.Function;

public class ColorHelper {
    private static Function<Object, Integer> dominantColorProvider = stack -> {
        if (stack == null) return 0;
        if (stack instanceof ItemStack apiStack) {
            if (apiStack.isEmpty()) return 0;
            return apiStack.itemId().hashCode() | 0xFF000000;
        }
        return stack.hashCode() | 0xFF000000;
    };

    public static void setDominantColorProvider(Function<Object, Integer> provider) {
        dominantColorProvider = provider;
    }

    public static int getDominantColor(Object stack) {
        return dominantColorProvider.apply(stack);
    }

    public static float[] colorToHSB(int color) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        return java.awt.Color.RGBtoHSB(r, g, b, null);
    }
}
