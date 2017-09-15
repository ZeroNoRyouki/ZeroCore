package it.zerono.mods.zerocore.lib.world;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public abstract class Zone implements Comparable<Zone> {

    public static Zone rectangular(final BlockPos minCoords, final BlockPos maxCoords) {
        return new RectangularCuboidZone(minCoords, maxCoords);
    }

    public static Zone sphere(final BlockPos center, int radius) {
        return new SphereZone(center, radius);
    }

    public abstract boolean contains(final BlockPos position);

    public abstract AxisAlignedBB getBoundingBox();

    @Override
    public int compareTo(Zone zone) {

        final AxisAlignedBB myBB = this.getBoundingBox();
        final AxisAlignedBB hisBB = zone.getBoundingBox();

        return 0;
    }

    private static class RectangularCuboidZone extends Zone {

        RectangularCuboidZone(final BlockPos minCoords, final BlockPos maxCoords) {

            this._minCoords = minCoords;
            this._maxCoords = maxCoords;
        }

        @Override
        public boolean contains(final BlockPos position) {

            return (this._minCoords.getX() <= position.getX() && position.getX() <= this._maxCoords.getX()) &&
                   (this._minCoords.getY() <= position.getY() && position.getY() <= this._maxCoords.getY()) &&
                   (this._minCoords.getZ() <= position.getZ() && position.getZ() <= this._maxCoords.getZ());
        }

        @Override
        public AxisAlignedBB getBoundingBox() {
            return new AxisAlignedBB(this._minCoords, this._maxCoords);
        }

        @Override
        public boolean equals(Object o) {

            if (this == o)
                return true;

            if (!(o instanceof RectangularCuboidZone))
                return false;

            RectangularCuboidZone other = (RectangularCuboidZone)o;

            return this._minCoords.equals(other._minCoords) && this._maxCoords.equals(other._maxCoords);
        }

        private final BlockPos _minCoords;
        private final BlockPos _maxCoords;
    }

    private static class SphereZone extends Zone {

        SphereZone(final BlockPos center, int radius) {

            this._center = center;
            this._radius = radius;
        }

        @Override
        public boolean contains(final BlockPos position) {

            return (((position.getX() - this._center.getX()) * (position.getX() - this._center.getX())) +
                    ((position.getY() - this._center.getY()) * (position.getY() - this._center.getY())) +
                    ((position.getZ() - this._center.getZ()) * (position.getZ() - this._center.getZ()))) <=
                    this._radius * this._radius;
        }

        @Override
        public AxisAlignedBB getBoundingBox() {

            final int centerX = this._center.getX();
            final int centerY = this._center.getY();
            final int centerZ = this._center.getZ();

            return new AxisAlignedBB(centerX - this._radius, centerY - this._radius, centerZ - this._radius,
                                     centerX + this._radius - 1, centerY + this._radius - 1, centerZ + this._radius - 1);
        }

        @Override
        public boolean equals(Object o) {

            if (this == o)
                return true;

            if (!(o instanceof SphereZone))
                return false;

            SphereZone other = (SphereZone)o;

            return this._radius == other._radius && this._center.equals(other._center);
        }

        private final BlockPos _center;
        private final int _radius;
    }
}