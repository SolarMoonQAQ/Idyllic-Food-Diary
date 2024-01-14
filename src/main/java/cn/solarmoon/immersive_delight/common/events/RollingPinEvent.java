package cn.solarmoon.immersive_delight.common.events;

import cn.solarmoon.immersive_delight.ImmersiveDelight;
import cn.solarmoon.immersive_delight.data.tags.IMBlockTags;
import cn.solarmoon.immersive_delight.init.Config;
import cn.solarmoon.immersive_delight.api.network.serializer.ServerPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static cn.solarmoon.immersive_delight.client.particles.vanilla.Sweep.sweep;
import static cn.solarmoon.immersive_delight.common.IMItems.ROLLING_PIN;

public class RollingPinEvent {

    //擀面杖快速收割
    @SubscribeEvent
    public void leftClickGet(PlayerInteractEvent.LeftClickBlock event) {
        if(!event.getItemStack().is(ROLLING_PIN.get())) return;
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        if(state.is(IMBlockTags.CAN_BE_ROLLED)) {
            //防止刷蛋糕
            if(state.is(Blocks.CAKE) && state.getValue(BlockStateProperties.BITES) != 0) return;
            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state.getBlock().asItem().getDefaultInstance());
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(0, 0.2, 0));
            level.destroyBlock(pos, false);
            level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 1.0f, 0.5f);
            level.addFreshEntity(itemEntity);
            event.getItemStack().hurtAndBreak(1, event.getEntity(), (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
            if(level.isClientSide) sweep();
        } else if (player.isCrouching()) {
            RollingPinEvent.harvest(0, player, level);
        }
    }

    /**
     * @param radius 0就根据配置文件来，其它的就是独自设置
     */
    public static void harvest(int radius, Player player, Level level) {
        int r = Config.areaHarvestRadius.get();
        if(radius == 0) radius = r;
        // 遍历玩家周围半径的方块
        BlockPos playerPos = player.blockPosition();
        for(int x = -radius; x <= radius; x++) {
            for(int y = 0; y <= radius; y++) {
                for(int z = -radius; z <= radius; z++) {
                    BlockPos pos = playerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    TagKey<Block> tag = BlockTags.create(new ResourceLocation(ImmersiveDelight.MOD_ID, "can_be_rolled"));
                    if(state.is(tag)) {
                        if(level.isClientSide) level.addParticle(ParticleTypes.SWEEP_ATTACK, pos.getX(), pos.getY(), pos.getZ(), 0,0,0);
                        ServerPackSerializer.sendPacket(pos, state.getBlock(), state.getBlock().asItem().getDefaultInstance(), "sweepHarvest");
                    }
                }
            }
        }
    }

}
