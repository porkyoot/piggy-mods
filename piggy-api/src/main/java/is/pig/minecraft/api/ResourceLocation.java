package is.pig.minecraft.api;

/**
 * Platform-agnostic resource identifier.
 */
public record ResourceLocation(String namespace, String path) {
    public static ResourceLocation of(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
    
    @Override
    public String toString() {
        return namespace + ":" + path;
    }
}
