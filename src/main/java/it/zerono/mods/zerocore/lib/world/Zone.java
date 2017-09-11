package it.zerono.mods.zerocore.lib.world;

import net.minecraft.util.math.BlockPos;

public abstract class Zone {

    public abstract boolean contains(final BlockPos position);

    public static Zone rectangular(final BlockPos minCoords, final BlockPos maxCoords) {
        return new RectangularCuboidZone(minCoords, maxCoords);
    }

    public static Zone sphere(final BlockPos center, int radius) {
        return new SphereZone(center, radius);
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

        private final BlockPos _center;
        private final int _radius;
    }
}