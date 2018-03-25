package it.zerono.mods.zerocore.lib.math;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import javax.annotation.Nonnull;

public class Vector3d {

    public double X;
    public double Y;
    public double Z;

    public Vector3d(final double x, final double y, final double z) {
        this.set(x, y, z);
    }

    public Vector3d(@Nonnull final Vector3d other) {
        this.set(other);
    }

    public Vector3d(@Nonnull final Vec3i other) {
        this.set(other);
    }

    @Nonnull
    public static Vector3d from(@Nonnull final Vec3i data) {
        return new Vector3d(data.getX(), data.getY(), data.getZ());
    }

    @Nonnull
    public static Vector3d fromCenter(@Nonnull final Vec3i data) {
        return from(data).add(0.5);
    }

    @Nonnull
    public static Vector3d from(@Nonnull final Entity data) {
        return new Vector3d(data.posX, data.posY, data.posZ);
    }

    @Nonnull
    public static Vector3d fromCenter(@Nonnull final Entity data) {
        return from(data).add(0.5);
    }

    @Nonnull
    public static Vector3d from(@Nonnull final TileEntity data) {
        return new Vector3d(data.getPos());
    }

    @Nonnull
    public static Vector3d fromCenter(@Nonnull final TileEntity data) {
        return from(data).add(0.5);
    }

    @Nonnull
    public static Vector3d from(@Nonnull final NBTTagCompound data) {
        return new Vector3d().loadFrom(data);
    }

    @Nonnull
    public Vec3i toVec3i() {
        return new Vec3i(this.X, this.Y, this.Z);
    }

    @Nonnull
    public BlockPos toBlockPos() {
        return new BlockPos(this.X, this.Y, this.Z);
    }

    @Nonnull
    public Vector3d loadFrom(@Nonnull final NBTTagCompound data) {

        this.X = data.getDouble("vx");
        this.Y = data.getDouble("vy");
        this.Z = data.getDouble("vz");
        return this;
    }

    @Nonnull
    public NBTTagCompound saveTo(@Nonnull final NBTTagCompound data) {

        data.setDouble("vx", this.X);
        data.setDouble("vy", this.Y);
        data.setDouble("vz", this.Z);
        return data;
    }

    @Nonnull
    public Vector3d set(final double x, final double y, final double z) {

        this.X = x;
        this.Y = y;
        this.Z = z;
        return this;
    }

    @Nonnull
    public Vector3d set(@Nonnull final Vector3d data) {
        return this.set(data.X, data.Y, data.Z);
    }

    @Nonnull
    public Vector3d set(@Nonnull final Vec3i data) {
        return this.set(data.getX(), data.getY(), data.getZ());
    }

    @Nonnull
    public Vector3d add(final double offsetX, final double offsetY, final double offsetZ) {

        this.X += offsetX;
        this.Y += offsetY;
        this.Z += offsetZ;
        return this;
    }

    @Nonnull
    public Vector3d add(final double offset) {
        return this.add(offset, offset, offset);
    }

    @Nonnull
    public Vector3d add(@Nonnull final Vector3d offset) {
        return this.add(offset.X, offset.Y, offset.X);
    }

    @Nonnull
    public Vector3d add(@Nonnull final Vec3i offset) {
        return this.add(offset.getX(), offset.getY(), offset.getZ());
    }

    @Nonnull
    public Vector3d subtract(final double offsetX, final double offsetY, final double offsetZ) {

        this.X -= offsetX;
        this.Y -= offsetY;
        this.Z -= offsetZ;
        return this;
    }

    @Nonnull
    public Vector3d subtract(final double offset) {
        return this.subtract(offset, offset, offset);
    }

    @Nonnull
    public Vector3d subtract(@Nonnull final Vec3i offset) {
        return this.subtract(offset.getX(), offset.getY(), offset.getZ());
    }

    @Nonnull
    public Vector3d multiply(final double factorX, final double factorY, final double factorZ) {

        this.X *= factorX;
        this.Y *= factorY;
        this.Z *= factorZ;
        return this;
    }

    @Nonnull
    public Vector3d multiply(final double factor) {
        return this.multiply(factor, factor, factor);
    }

    @Nonnull
    public Vector3d multiply(@Nonnull final Vec3i factor) {
        return this.multiply(factor.getX(), factor.getY(), factor.getZ());
    }

    @Nonnull
    public Vector3d divide(final double factorX, final double factorY, final double factorZ) {

        this.X /= factorX;
        this.Y /= factorY;
        this.Z /= factorZ;
        return this;
    }

    @Nonnull
    public Vector3d divide(final double factor) {
        return this.divide(factor, factor, factor);
    }

    @Nonnull
    public Vector3d divide(@Nonnull final Vec3i factor) {
        return this.divide(factor.getX(), factor.getY(), factor.getZ());
    }

    @Nonnull
    public Vector3d ceil() {

        this.X = Math.ceil(this.X);
        this.Y = Math.ceil(this.Y);
        this.Z = Math.ceil(this.Z);
        return this;
    }

    @Nonnull
    public Vector3d floor() {

        this.X = Math.floor(this.X);
        this.Y = Math.floor(this.Y);
        this.Z = Math.floor(this.Z);
        return this;
    }

    public double magnitude() {
        return Math.sqrt(this.X * this.X + this.Y * this.Y + this.Z * this.Z);
    }

    @Nonnull
    public Vector3d normalize() {

        final double magnitude = this.magnitude();

        if (0 != magnitude) {
            this.multiply(1.0 / magnitude);
        }

        return this;
    }

    public double scalarProduct(final double x, final double y, final double z) {
        return this.X * x + this.Y * y + this.Z * z;
    }

    public double scalarProduct(@Nonnull final Vector3d vector) {

        double product = this.X * vector.X + this.Y * vector.Y + this.Z * vector.Z;

        if (product > 1.0 && product < 1.00001) {

            product = 1.0;

        } else if (product < -1 && product > -1.00001) {

            product = -1.0;
        }

        return product;
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof Vector3d) {

            Vector3d v = (Vector3d)other;

            return this.X == v.X && this.Y == v.Y && this.Z == v.Z;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("Vector3d (%f, %f, %f)", this.X, this.Y, this.Z);
    }

    private Vector3d() {
    }
}