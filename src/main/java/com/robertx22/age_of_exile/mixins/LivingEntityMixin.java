package com.robertx22.age_of_exile.mixins;

import com.robertx22.age_of_exile.event_hooks.damage_hooks.LivingHurtUtils;
import com.robertx22.age_of_exile.event_hooks.damage_hooks.util.DmgSourceUtils;
import com.robertx22.age_of_exile.mixin_ducks.LivingEntityAccesor;
import com.robertx22.age_of_exile.mixin_methods.CanEntityHavePotionMixin;
import com.robertx22.age_of_exile.uncommon.utilityclasses.HealthUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityAccesor {

    @Shadow
    protected abstract float getVoicePitch();

    @Override
    @Invoker("blockedByShield")
    public abstract void myknockback(LivingEntity target);

    @Override
    @Invoker("getHurtSound")
    public abstract SoundEvent myGetHurtSound(DamageSource source);

    @Override
    @Invoker("getSoundVolume")
    public abstract float myGetHurtVolume();

    @Override
    @Invoker("getVoicePitch")
    public abstract float myGetHurtPitch();

    @ModifyVariable(method = "heal(F)V", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    public float reduceHealPerLevel(float amount, float arg) {
        LivingEntity en = (LivingEntity) (Object) this;
        return HealthUtils.realToVanilla(en, amount);

    }

    // ENSURE MY SPECIAL DAMAGE ISNT LOWERED BY ARMOR, ENCHANTS ETC
    @Inject(method = "getDamageAfterMagicAbsorb", at = @At(value = "HEAD"), cancellable = true)
    public void hookench(DamageSource source, float amount, CallbackInfoReturnable<Float> ci) {
        LivingEntity en = (LivingEntity) (Object) this;
        if (DmgSourceUtils.isMyDmgSource(source)) {
            ci.setReturnValue(amount);
        }

    } // ENSURE MY SPECIAL DAMAGE ISNT LOWERED BY ARMOR, ENCHANTS ETC


    @Inject(method = "getDamageAfterMagicAbsorb", at = @At(value = "RETURN"), cancellable = true)
    public void hookenchreturn(DamageSource source, float amount, CallbackInfoReturnable<Float> ci) {
        LivingEntity en = (LivingEntity) (Object) this;

        if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            LivingHurtUtils.damageCurioItems(en, amount);
        }
        if (DmgSourceUtils.isMyDmgSource(source)) {
            ci.setReturnValue(amount);
        }

    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), cancellable = true)
    public void hookarmortodmg(DamageSource source, float amount, CallbackInfoReturnable<Float> ci) {
        LivingEntity en = (LivingEntity) (Object) this;
        if (DmgSourceUtils.isMyDmgSource(source)) {
            //damageArmor(source, MathHelper.clamp(amount, 2, 10));
            ci.setReturnValue(amount);
        }
    }
    // ENSURE MY SPECIAL DAMAGE ISNT LOWERED BY ARMOR, ENCHANTS ETC

    @Inject(method = "canBeAffected", at = @At(value = "HEAD"), cancellable = true)
    public void hook(MobEffectInstance effect, CallbackInfoReturnable<Boolean> ci) {
        try {
            LivingEntity en = (LivingEntity) (Object) this;
            CanEntityHavePotionMixin.hook(en, effect, ci);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "HEAD", ordinal = 0), argsOnly = false, ordinal = 0)
    public float reduceHealPerLevel(float amount, DamageSource source) {
        if (source instanceof DamageSourceDuck du) {
            if (du.hasMnsDamageOverride()) {
                return du.getMnsDamage();
            }
        }
        return amount;

    }

 */

}
