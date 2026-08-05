package net.erutobusiness.minecarttrainspeed.mixin;

import net.erutobusiness.minecarttrainspeed.MinecartTrainSpeed;
import net.lordkipama.modernminecarts.entity.CustomAbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * 連結時に直書きされている {@code 0.8f} を、設定に追従する値へ差し替える。
 *
 * <p>元のコード（{@code getMaxSpeedWithRail} の抜粋）:
 * <pre>
 * float f = ((BaseRailBlock) state.getBlock()).getRailMaxSpeed(...);
 * if (state.is(Blocks.POWERED_RAIL)) f = ModernMinecartsConfig.poweredRailSpeed();
 * if (this.getLinkedParent() != null) f = 0.8f;   // ← ここ。config を見ていない
 * </pre>
 *
 * <p>⚠ {@code 0.8f} はこのメソッドの中に<b>1回だけ</b>しか出てこないので、
 * {@code @ModifyConstant} が別の箇所を巻き込むことはない（逆アセンブルで数えて確認）。
 *
 * <p>⚠ {@code remap = false}。対象は Minecraft ではなく他MODのクラスとメソッドなので、
 * 名前を SRG へ写してはいけない。
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
