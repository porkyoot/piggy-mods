package is.pig.minecraft.api;

/**
 * Pure Java equivalent of Minecraft Direction.
 */
public enum Direction {
    DOWN(0, -1, 0, Axis.Y),
    UP(0, 1, 0, Axis.Y),
    NORTH(0, 0, -1, Axis.Z),
    SOUTH(0, 0, 1, Axis.Z),
    WEST(-1, 0, 0, Axis.X),
    EAST(1, 0, 0, Axis.X);

    private final int x, y, z;
    private final Axis axis;

    Direction(int x, int y, int z, Axis axis) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.axis = axis;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public Axis getAxis() { return axis; }

    public enum Axis {
        X, Y, Z
    }
}
