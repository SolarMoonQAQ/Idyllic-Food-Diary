package cn.solarmoon.immersive_delight.api.registry;

import cn.solarmoon.immersive_delight.api.network.INetWorkReg;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本网络数据包注册表
 */
public abstract class BasePackRegistry {

    protected List<INetWorkReg> packs;

    public BasePackRegistry() {
        this.packs = new ArrayList<>();
    }

    /**
     * 这里默认只能添加到服务端和客户端的包各一个
     */
    public abstract void addRegistry();

    public void register() {
        addRegistry();
        for (INetWorkReg pack : packs) {
            pack.register();
        }
    }

}
