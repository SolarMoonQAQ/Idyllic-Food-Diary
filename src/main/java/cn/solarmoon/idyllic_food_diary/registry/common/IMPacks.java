package cn.solarmoon.idyllic_food_diary.registry.common;

import cn.solarmoon.idyllic_food_diary.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.network.ClientPackHandler;
import cn.solarmoon.idyllic_food_diary.network.ServerPackHandler;
import cn.solarmoon.solarmoon_core.api.entry.common.NetPackEntry;


public class IMPacks {
    public static void register() {}

    public final static NetPackEntry CLIENT_PACK = IdyllicFoodDiary.REGISTRY.netPack()
            .id("client")
            .side(NetPackEntry.Side.CLIENT)
            .addHandler(new ClientPackHandler())
            .build();

    public final static NetPackEntry SERVER_PACK = IdyllicFoodDiary.REGISTRY.netPack()
            .id("server")
            .side(NetPackEntry.Side.SERVER)
            .addHandler(new ServerPackHandler())
            .build();

}
