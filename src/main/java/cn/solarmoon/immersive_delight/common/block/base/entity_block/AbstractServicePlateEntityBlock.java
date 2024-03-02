package cn.solarmoon.immersive_delight.common.block.base.entity_block;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.common.block_entity.base.AbstractServicePlateBlockEntity;
import cn.solarmoon.immersive_delight.common.registry.IMBlockEntities;
import cn.solarmoon.solarmoon_core.common.block.entity_block.BaseContainerEntityBlock;
import cn.solarmoon.solarmoon_core.util.CountingDevice;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static cn.solarmoon.immersive_delight.client.particle.vanilla.Eat.eat;

public class AbstractServicePlateEntityBlock extends BaseContainerEntityBlock {

    private final int eatCount = 4;

    public AbstractServicePlateEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        AbstractServicePlateBlockEntity plate = (AbstractServicePlateBlockEntity) level.getBlockEntity(pos);

        if (plate == null) return InteractionResult.PASS;
        if (putItem(plate, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        if (player.isCrouching() && takeItem(plate, player, hand, 1)) {
            level.playSound(null, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.5f, 1f);
            return InteractionResult.SUCCESS;
        }
        ItemStack food = plate.getLastItem();
        if (player.canEat(false) && food.isEdible()) {
            //计数装置
            CountingDevice counting = new CountingDevice(player, pos);
            ImmersiveDelight.DEBUG.send("点击次数：" + counting.getCount(), level);
            //吃的声音
            level.playSound(player, pos, getEatSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            //吃的粒子效果
            if(level.isClientSide) eatParticle(food);
            if (counting.getCount() >= this.eatCount) {
                //吃掉！
                ItemStack give = food.finishUsingItem(level, player);
                if (!player.isCreative()) LevelSummonUtil.addItemToInventory(player, give);
                else food.shrink(1); //创造模式也消耗食物
                //重置计数
                counting.resetCount();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public SoundEvent getEatSound() {
        return SoundEvents.GENERIC_EAT;
    }

    /**
     * 决定吃的粒子效果
     */
    public void eatParticle(ItemStack stack) {
        eat(stack);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return IMBlockEntities.PLATE.get();
    }

}
