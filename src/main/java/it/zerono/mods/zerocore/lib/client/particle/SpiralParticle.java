package it.zerono.mods.zerocore.lib.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SpiralParticle extends Particle {

    protected SpiralParticle(World world, double centerX, double centerY, double centerZ, double radius, int lifeInTicks) {

        super(world, centerX + radius * /*MathHelper.cos(0)*/1, centerY, centerZ + radius * /*MathHelper.sin(0)*/0);
        this._angle = 0;
        this._centerX = centerX;
        this._centerZ = centerZ;
        this._radius = radius;
        this.particleMaxAge = lifeInTicks;
    }

    @Override
    public void onUpdate() {

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (++this.particleAge >= this.particleMaxAge)
            this.setExpired();

        if ((this._angle += 10) >= 360)
            this._angle = 0;

        float radiants = (float)(this._angle * Math.PI / 180.0);
        double newX = this._centerX + this._radius * MathHelper.cos(radiants);
        double newZ = this._centerZ + this._radius * MathHelper.sin(radiants);

        this.motionX = newX - this.posX;
        this.motionZ = newZ - this.posZ;
        this.motionY = 0.01;

        this.move(this.motionX, this.motionY, this.motionZ);
    }

    protected float _angle;
    protected double _centerX;
    protected double _centerZ;
    protected double _radius;
}