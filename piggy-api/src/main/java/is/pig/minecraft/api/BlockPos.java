package is.pig.minecraft.api;

public record BlockPos(int x, int y, int z) {
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    public BlockPos offset(int dx, int dy, int dz) {
        return new BlockPos(x + dx, y + dy, z + dz);
    }

    public BlockPos relative(Direction direction) {
        return switch (direction) {
            case DOWN -> offset(0, -1, 0);
            case UP -> offset(0, 1, 0);
            case NORTH -> offset(0, 0, -1);
            case SOUTH -> offset(0, 0, 1);
            case WEST -> offset(-1, 0, 0);
            case EAST -> offset(1, 0, 0);
        };
    }

    public BlockPos above() {
        return relative(Direction.UP);
    }

    public BlockPos below() {
        return relative(Direction.DOWN);
    }
}
