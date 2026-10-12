package net.erutobusiness.minecarttrainspeed.mixin;

import net.erutobusiness.minecarttrainspeed.MinecartTrainSpeed;
import net.lordkipama.modernminecarts.entity.CustomAbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * 連結時に直書きされている {@code 0.8f} を、設定に追従する値へ差し替える。
 *
 * <p>{@code getMaxSpeedWithRail} は、レールごとの上限を求め、動力レールなら
 * Modern Minecarts 自身の config を読んで上書きする。ところが<b>連結時の分岐だけ</b>は
 * config を見ず、<b>定数 {@code 0.8f} を代入している</b>（逆アセンブルで確認）。
 *
 * <p>⚠ 上流は All Rights Reserved なので、ここにソースをコピーしない。
 *    書いてよいのは「どういう振る舞いか」までで、実装そのものではない。
 *
 * <p>⚠ {@code 0.8f} はこのメソッドの中に<b>1回だけ</b>しか出てこないので、
 * {@code @ModifyConstant} が別の箇所を巻き込むことはない（逆アセンブルで数えて確認）。
 *
 * <p>⚠ {@code remap = false}。対象は Minecraft ではなく他MODのクラスとメソッドなので、
 * 名前を SRG へ変換してはいけない。
 */
@Mixin(value = CustomAbstractMinecartEntity.class, remap = false)
public abstract class CustomAbstractMinecartEntityMixin {

    @ModifyConstant(
            method = "getMaxSpeedWithRail",
            constant = @Constant(floatValue = 0.8F),
            remap = false)
    private float minecart_train_speed$linkedSpeed(float original) {
        return MinecartTrainSpeed.linkedMaxSpeed();
    }
}
