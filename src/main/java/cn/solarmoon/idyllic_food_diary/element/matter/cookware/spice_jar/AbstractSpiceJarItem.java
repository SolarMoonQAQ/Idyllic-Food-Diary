package cn.solarmoon.idyllic_food_diary.element.matter.cookware.spice_jar;

import cn.solarmoon.idyllic_food_diary.feature.spice.ISpiceable;
import cn.solarmoon.idyllic_food_diary.feature.spice.Spice;
import cn.solarmoon.solarmoon_core.api.item_util.IContainerItem;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public abstract class AbstractSpiceJarItem extends BlockItem implements IContainerItem {

    public AbstractSpiceJarItem(Block block) {
        super(block, new Properties().stacksTo(1));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ISpiceable sp && hasEnoughSpice(stack) && !sp.timeToResetSpices()) {
            ItemStackHandler inv = ContainerUtil.getInventory(stack);
            sp.getSpices().add(new Spice(inv.extractItem(0, 1, false).getItem(), sp.getSpiceStep()));
            level.playSound(null, pos, SoundEvents.AZALEA_LEAVES_HIT, SoundSource.PLAYERS);
            makeSpiceParticle(level, pos);
            if (player != null && !player.isCreative()) ContainerUtil.setInventory(stack, inv);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    public static void makeSpiceParticle(Level level, BlockPos pos) {
        Random random = new Random();
        for (int i = 0; i < 7; ++i) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            level.addParticle(ParticleTypes.COMPOSTER,
                    pos.getX() + random.nextFloat(),
                    pos.getY() + random.nextDouble(),
                    pos.getZ() + random.nextFloat(),
                    d0, d1, d2);
        }
    }

    public static boolean hasEnoughSpice(ItemStack stack) {
        return !ContainerUtil.getInventory(stack).getStackInSlot(0).isEmpty();
    }

}
