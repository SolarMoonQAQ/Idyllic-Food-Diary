package cn.solarmoon.idyllic_food_diary.core.common.registry;

import cn.solarmoon.idyllic_food_diary.core.IdyllicFoodDiary;
import cn.solarmoon.idyllic_food_diary.core.network.ClientPackHandler;
import cn.solarmoon.idyllic_food_diary.core.network.ServerPackHandler;
import cn.solarmoon.solarmoon_core.api.common.registry.NetPackEntry;


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
