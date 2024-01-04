package cn.solarmoon.immersive_delight.common.blocks.abstract_blocks;

/**
 * 基本的烧水壶抽象类
 * 应用tankBlockEntity，是一个液体容器（但是不使其具有原有的液体交互功能，而是替换为倒水功能）
 * 具有烧水、倒水功能
 */
public abstract class KettleEntityBlock extends BasicEntityBlock {

    protected KettleEntityBlock(Properties properties) {
        super(properties);
    }

}
