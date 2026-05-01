package is.pig.minecraft.api;

public record ItemStack(String itemId, int count) {
    public boolean isEmpty() {
        return itemId == null || itemId.isEmpty() || count <= 0;
    }
}
