package is.pig.minecraft.api;

public record Vec3(double x, double y, double z) {
    public static Vec3 atCenterOf(BlockPos pos) {
        return new Vec3(pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5);
    }
    
    public double distanceToSqr(Vec3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    public Vec3 subtract(Vec3 other) {
        return new Vec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }
    
    public Vec3 normalize() {
        double d = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (d < 1.0E-4D) {
            return new Vec3(0, 0, 0);
        }
        return new Vec3(this.x / d, this.y / d, this.z / d);
    }
    
    public double dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
}
