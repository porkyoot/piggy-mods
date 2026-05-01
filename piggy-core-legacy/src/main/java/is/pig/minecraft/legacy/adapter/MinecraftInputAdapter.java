package is.pig.minecraft.legacy.adapter;

import is.pig.minecraft.api.spi.InputAdapter;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.HashMap;
import java.util.Map;

public class MinecraftInputAdapter implements InputAdapter {
    private final Map<String, KeyMapping> keyMappings = new HashMap<>();

    @Override
    public boolean isKeyDown(String keyId) {
        KeyMapping mapping = keyMappings.get(keyId);
        if (mapping != null) {
            return mapping.isDown();
        }
        // Fallback for standard keys if not registered
        if (keyId.equals("minecraft:attack")) return Minecraft.getInstance().options.keyAttack.isDown();
        if (keyId.equals("minecraft:use")) return Minecraft.getInstance().options.keyUse.isDown();
        return false;
    }

    @Override
    public void setKeyDown(String keyId, boolean down) {
        KeyMapping mapping = keyMappings.get(keyId);
        if (mapping != null) {
            mapping.setDown(down);
        } else {
            // Fallback for standard keys
            if (keyId.equals("minecraft:attack")) Minecraft.getInstance().options.keyAttack.setDown(down);
            if (keyId.equals("minecraft:use")) Minecraft.getInstance().options.keyUse.setDown(down);
        }
    }

    @Override
    public void registerKey(String keyId, String defaultKey, String category) {
        if (keyMappings.containsKey(keyId)) return;

        int keyCode = parseKey(defaultKey);
        KeyMapping mapping = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                keyId,
                InputConstants.Type.KEYSYM,
                keyCode,
                category
        ));
        keyMappings.put(keyId, mapping);
    }

    private int parseKey(String key) {
        try {
            return (int) com.mojang.blaze3d.platform.InputConstants.class.getDeclaredField(key).get(null);
        } catch (Exception e) {
            return -1;
        }
    }
}
