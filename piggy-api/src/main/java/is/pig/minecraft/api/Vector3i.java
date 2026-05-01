package is.pig.minecraft.api;

/**
 * An immutable 3D integer vector for pure-Java block coordinates.
 */
public record Vector3i(int x, int y, int z) {
    
    public Vector3i add(int dx, int dy, int dz) {
        return new Vector3i(x + dx, y + dy, z + dz);
    }
    
    public Vector3i add(Vector3i other) {
        return new Vector3i(x + other.x, y + other.y, z + other.z);
    }
    
    public Vector3i subtract(Vector3i other) {
        return new Vector3i(x - other.x, y - other.y, z - other.z);
    }
    
    public double distanceTo(Vector3i other) {
        int dx = x - other.x;
        int dy = y - other.y;
        int dz = z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }
}
