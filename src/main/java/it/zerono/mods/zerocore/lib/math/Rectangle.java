package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class Rectangle {

    public int X;
    public int Y;
    public int WIDTH;
    public int HEIGHT;

    public Rectangle(final int x, final int y, final int width, final int height) {
        this.set(x, y, width, height);
    }

    public Rectangle(@Nonnull final Rectangle other) {
        this.set(other);
    }

    public int getX1() {
        return this.X;
    }

    public int getY1() {
        return this.Y;
    }

    public int getX2() {
        return this.X + this.WIDTH - 1;
    }

    public int getY2() {
        return this.Y + this.HEIGHT - 1;
    }

    @Nonnull
    public Rectangle set(final int x, final int y, final int width, final int height) {

        this.X = x;
        this.Y = y;
        this.WIDTH = Math.abs(width);
        this.HEIGHT = Math.abs(height);
        return this;
    }

    @Nonnull
    public Rectangle set(@Nonnull final Rectangle other) {
        return this.set(other.X, other.Y, other.WIDTH, other.HEIGHT);
    }

    @Nonnull
    public Rectangle offset(final int offsetX, final int offsetY) {

        this.X += offsetX;
        this.Y += offsetY;
        return this;
    }

    @Nonnull
    public Rectangle expand(final int deltaX, final int deltaY) {

        if (deltaX > 0) {

            this.WIDTH += deltaX;

        } else {

            this.X += deltaX;
            this.WIDTH -= deltaX;
        }

        if (deltaY > 0) {

            this.HEIGHT += deltaY;

        } else {

            this.Y += deltaY;
            this.HEIGHT -= deltaY;
        }

        return this;
    }

    @Nonnull
    public Rectangle wrap(final int x, final int y) {

        if (x < this.X) {
            this.expand(x - this.X, 0);
        } else if (x >= this.X + this.WIDTH) {
            this.expand(x - this.X - this.WIDTH + 1, 0);
        }

        if (y < this.Y) {
            this.expand(0, y - this.Y);
        } else if (y >= this.Y + this.HEIGHT) {
            this.expand(0, y - this.Y - this.HEIGHT + 1);
        }

        return this;
    }

    @Nonnull
    public Rectangle wrap(@Nonnull final Rectangle other) {

        this.wrap(other.getX1(), other.getY1());
        this.wrap(other.getX2(), other.getY2());
        return this;
    }

    public boolean contains(int x, int y) {
        return this.getX1() <= x && x <= this.getX2() &&
                this.getY1() <= y && y <= this.getY2();
    }

    public boolean intersects(@Nonnull final Rectangle other) {
        return other.X + other.WIDTH > this.X && other.X < this.X + this.WIDTH &&
                other.Y + other.HEIGHT > this.Y && other.Y < this.Y + this.HEIGHT;
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof Rectangle) {

            Rectangle r = (Rectangle)other;

            return this.X == r.X && this.Y == r.Y && this.WIDTH == r.WIDTH && this.HEIGHT == r.HEIGHT;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("Rectangle (%d, %d) [%d x %d]", this.X, this.Y, this.WIDTH, this.HEIGHT);
    }

    private Rectangle() {
    }
}