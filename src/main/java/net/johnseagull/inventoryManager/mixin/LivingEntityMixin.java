package net.johnseagull.inventoryManager.mixin;

import net.johnseagull.inventoryManager.accessor.LivingEntityAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityAccessor {
    @Unique
    public int combatTime = 0;

    @Inject(method="handleDamageEvent",at=@At("TAIL"))
    public void handleDamageEvent(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.getEntity() instanceof ServerPlayer me) {
            this.combatTime = 0;
        }
    }

    @Override
    public int getCombatTime() {
        return combatTime;
    }

    @Override
    public void setCombatTime(int time) {
        combatTime = time;
    }
}
