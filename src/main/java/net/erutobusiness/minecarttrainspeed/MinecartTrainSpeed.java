package net.erutobusiness.minecarttrainspeed;

import net.lordkipama.modernminecarts.ModernMinecartsConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * 連結したトロッコの速度上限を、銅レールの設定に合わせる。
 *
 * <p>Modern Minecarts の {@code CustomAbstractMinecartEntity.getMaxSpeedWithRail} は
 * レールごとの上限を求めたあと、<b>連結していたら無条件に {@code 0.8f} を代入する</b>
 * （{@code getLinkedParent() != null → f = 0.8f}）。この 0.8 は config を見ない直書きで、
 * {@code copper_speed} の既定値とたまたま同じ値になっている。
 *
 * <p>当部は幹線を 1.2（24 b/s）へ上げたため、<b>連結した瞬間に 16 b/s へ落ちて
 * 道の上の馬（21〜23 b/s）より遅くなる</b>という逆転が起きていた。
 * ここでその定数だけを差し替える。
 *
 * <p>⚠ {@code 0.8f} は {@code getMaxSpeedWithRail} の中に<b>ちょうど1回</b>しか出てこない
 * （逆アセンブルで確認）。だから {@code @ModifyConstant} が別の場所を巻き込むことはない。
 *
 * <p>作者にも報告する。取り込まれたら本MODは役目を終える。
 */
@Mod(MinecartTrainSpeed.MOD_ID)
public class MinecartTrainSpeed {

    public static final String MOD_ID = "minecart_train_speed";

    public static final ForgeConfigSpec SPEC;

    /**
     * 連結時の速度上限（1 tick あたりのブロック数）。
     * 0 以下なら Modern Minecarts の {@code copper_speed} に追従する（既定）。
     */
    public static final ForgeConfigSpec.DoubleValue LINKED_MAX_SPEED;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.comment("連結したトロッコの速度上限").push("linked");
        LINKED_MAX_SPEED = b
                .comment("1 tick あたりのブロック数。20 倍が秒速。",
                         "0 以下なら Modern Minecarts の copper_speed に追従する（推奨）。",
                         "元の実装は 0.8 の直書きで、config を見ていなかった。")
                .defineInRange("maxSpeed", 0.0D, 0.0D, 1.6D);
        b.pop();
        SPEC = b.build();
    }

    public MinecartTrainSpeed() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC);
    }

    /** mixin から呼ぶ。config が 0 以下なら銅レールの設定に追従する。 */
    public static float linkedMaxSpeed() {
        double v = LINKED_MAX_SPEED.get();
        return v > 0.0D ? (float) v : ModernMinecartsConfig.copperSpeed();
    }
}
