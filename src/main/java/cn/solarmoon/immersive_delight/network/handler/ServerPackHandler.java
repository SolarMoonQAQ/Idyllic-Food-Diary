package cn.solarmoon.immersive_delight.network.handler;

import cn.solarmoon.immersive_delight.network.serializer.ServerPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

public class ServerPackHandler {
    public static void serverPackHandler(ServerPackSerializer packet, NetworkEvent.Context context) {

        //快乐的定义时间
        ServerPlayer player = context.getSender();
        Level level = null;
        if (player != null) {
            level = player.level();
        }
        BlockPos pos = packet.pos();
        ItemStack stack = packet.stack();
        Block block = null;
        if (level != null) {
            if (pos != null) {
                block = level.getBlockState(pos).getBlock();
            }
        }
        Block realBlock = packet.block();
        //处理
        switch (packet.message()) {
            //处理长按吃掉的食物方块
            case "eat" -> {
                if(block == null) return;
                ItemStack food = block.asItem().getDefaultInstance();
                player.eat(level, food);
            }
            case "finished" -> {
                if(level == null || pos == null || realBlock == null) return;
                level.setBlock(pos, realBlock.defaultBlockState(), 3);
            }
            //结束使用物品
            case "stopUse" -> {
                if(player == null) return;
                player.stopUsingItem();
            }
            //擀面结束的输出
            case "rollingOutput" -> {
                if(realBlock == null || level == null || pos == null || stack == null) return;
                level.destroyBlock(pos, false);
                level.setBlock(pos, realBlock.defaultBlockState(), 3);
            }
            case "rollingResults" -> {
                if(level == null || pos == null || stack == null) return;
                double maxY = level.getBlockState(pos).getShape(level, pos).max(Direction.Axis.Y);
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + maxY / 2, pos.getZ() + 0.5, stack);
                level.addFreshEntity(itemEntity);
            }
            //擀面杖收割技能
            case "sweepHarvest" -> {
                if(level == null || pos == null || stack == null) return;
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, block.asItem().getDefaultInstance());
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(0, 0.2, 0));
                level.destroyBlock(pos, false);
                for(int i = 0; i < 5; i++) {
                    level.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 2.0f, (1.1f- (float) i /5));
                }
                level.addFreshEntity(itemEntity);
                stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
            }
        }
    }

}
