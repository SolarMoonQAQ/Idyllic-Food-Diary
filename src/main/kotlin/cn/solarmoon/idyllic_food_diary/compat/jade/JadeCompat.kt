package cn.solarmoon.idyllic_food_diary.compat.jade

import cn.solarmoon.idyllic_food_diary.element.matter.cookware.kettle.KettleBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.millstone.MillstoneBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.steamer.SteamerBlock
import cn.solarmoon.idyllic_food_diary.element.matter.cookware.wok.WokBlock
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin

@WailaPlugin
class JadeCompat: IWailaPlugin {

    override fun registerClient(registration: IWailaClientRegistration) {
        registration.registerBlockComponent(CommonCookwareProvider("wok"), WokBlock::class.java)
        registration.registerBlockComponent(CommonCookwareProvider("millstone"), MillstoneBlock::class.java)
        registration.registerBlockComponent(CommonCookwareProvider("steamer"), SteamerBlock::class.java)
        registration.registerBlockComponent(CommonCookwareProvider("kettle"), KettleBlock::class.java)
    }

}