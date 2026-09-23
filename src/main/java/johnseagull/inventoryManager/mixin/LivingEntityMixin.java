package johnseagull.inventoryManager.mixin;

import johnseagull.figManagerMC.FigManagerMC;
import johnseagull.inventoryManager.accessor.LivingEntityAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityAccessor {
    @Unique
    public int combatTime = 0;
    @Inject(method="hurtServer",at=@At("TAIL"))
    public void handleDamageEvent(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {

        if (damageSource.getEntity() instanceof ServerPlayer) {
            this.combatTime = 0;
        }
        if (damageSource.type().toString().contains("player")) {
            this.combatTime = 0;
        }
        IO.println("ow");
    }
    @Inject(method="tick",at=@At("TAIL"))
    public void tick(CallbackInfo ci) {

        if ((LivingEntity)(Object)this instanceof ServerPlayer) {
            this.combatTime += 1;

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
