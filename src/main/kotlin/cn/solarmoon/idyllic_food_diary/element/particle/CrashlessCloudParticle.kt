package cn.solarmoon.idyllic_food_diary.element.particle

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.PlayerCloudParticle
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.max

class CrashlessCloudParticle(level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double, val sprites: SpriteSet):
    PlayerCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites) {

    override fun tick() {
        this.xo = this.x
        this.yo = this.y
        this.zo = this.z
        if (this.age++ >= this.lifetime) {
            this.remove()
        } else {
            this.yd = this.yd - 0.04 * this.gravity
            this.move(this.xd, this.yd, this.zd)
            if (this.speedUpWhenYMotionIsBlocked && this.y == this.yo) {
                this.xd *= 1.1
                this.zd *= 1.1
            }

            this.xd = this.xd * this.friction
            this.yd = this.yd * this.friction
            this.zd = this.zd * this.friction
            if (this.onGround) {
                this.xd *= 0.7F
                this.zd *= 0.7F
            }
        }

        if (!removed) this.setSpriteFromAge(sprites)
    }

    class Provider(private val sprites: SpriteSet): ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType, level: ClientLevel, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double
        ): Particle {
            return CrashlessCloudParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites)
        }
    }

}