package cn.solarmoon.idyllic_food_diary.element.recipe.assistant

import cn.solarmoon.spark_core.api.recipe.processor.IProcessorAssistant
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player

interface IExpGiver: IProcessorAssistant {

    var exp: Int

    /**
     * 给予玩家储存的经验，给予完后经验会重置为0
     * @param reset 是否重置经验为0
     */
    fun giveExp(player: Player, reset: Boolean): Boolean {
        if (exp != 0) {
            player.giveExperiencePoints(exp)
            player.level().playSound(null, player.onPos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS)
            if (reset) resetExp()
            return true
        }
        return false
    }

    fun resetExp() {
        exp = 0
    }

    override fun aSave(tag: CompoundTag, registries: HolderLookup.Provider) {
        tag.putInt("Exp", exp)
    }

    override fun aLoad(tag: CompoundTag, registries: HolderLookup.Provider) {
        exp = tag.getInt("Exp")
    }

}