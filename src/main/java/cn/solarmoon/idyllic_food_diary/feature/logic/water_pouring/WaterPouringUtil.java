package cn.solarmoon.idyllic_food_diary.feature.logic.water_pouring;

import cn.solarmoon.idyllic_food_diary.feature.logic.tea_brewing.TeaBrewingUtil;
import cn.solarmoon.idyllic_food_diary.registry.common.IMSounds;
import cn.solarmoon.idyllic_food_diary.util.ParticleSpawner;
import cn.solarmoon.solarmoon_core.api.common.item.ITankItem;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import cn.solarmoon.solarmoon_core.api.util.phys.OrientedBox;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class WaterPouringUtil {

    public static void doPouring(Player player, ServerLevel level, BlockPos pos) {
        ItemStack itemStack = player.getMainHandItem(); // 必须使用主手而非发来的item，因为发来的item相当于copy了一个，但不再是原来的那个了
        if(itemStack.getItem() instanceof ITankItem) {
            IFluidHandlerItem tankStack = FluidUtil.getTank(itemStack);
            FluidStack fluidStack0 = tankStack.getFluidInTank(0);
            int fluidAmount = fluidStack0.getAmount();
            ParticleSpawner.fluidPouring(fluidStack0, player, level);

            //要产生效果至少要符合最低的标准量
            int needAmount = TeaBrewingUtil.getDrinkVolume(level, fluidStack0);

            if(fluidAmount >= needAmount) {
                OrientedBox box = new OrientedBox(player, 2, 2, 3);
                Iterable<Entity> entities = level.getAllEntities();
                for(var entity : entities) {
                    if (entity instanceof LivingEntity living) {
                        if (box.intersects(living.getBoundingBox()) && !living.is(player)) {
                            // 众所周知液体穿墙是特性（点名表扬喷溅药水）
                            TeaBrewingUtil.commonDrink(fluidStack0, living, false);
                        }
                    }
                }
                tankStack.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
                level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
                level.playSound(null, pos, IMSounds.PLAYER_SPILLING_WATER.get(), SoundSource.PLAYERS, 1F, 1F);
            }
        }
    }

}
