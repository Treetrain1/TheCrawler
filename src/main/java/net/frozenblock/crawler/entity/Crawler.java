package net.frozenblock.crawler.entity;

import com.mojang.serialization.Dynamic;
import net.frozenblock.crawler.entity.ai.CrawlerBrain;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class Crawler extends HostileEntity {

    public Crawler(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 10;
        this.getNavigation().setCanSwim(true);
        this.setPathfindingPenalty(PathNodeType.UNPASSABLE_RAIL, 0.0F);
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return 0.0F;
    }

    @Override
    public boolean disablesShield() {
        return true;
    }

    public static DefaultAttributeContainer.Builder addAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.2)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10);
    }

    @Override
    public boolean occludeVibrationSignals() {
        return true;
    }

    @Override
    protected float getSoundVolume() {
        return 1.5F;
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        } else {
            this.world.sendEntityStatus(this, EntityStatuses.PLAY_ATTACK_SOUND);
            return super.tryAttack(target);
        }
    }

    @Override
    public void tick() {

        super.tick();
    }

    @Override
    public void mobTick() {
        ServerWorld serverWorld = (ServerWorld) this.world;
        serverWorld.getProfiler().push("crawlerBrain");
        this.getBrain().tick(serverWorld, this);
        this.world.getProfiler().pop();
        super.mobTick();
    }

    @Override
    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return CrawlerBrain.create(this, dynamic);
    }

    @Override
    public Brain<Crawler> getBrain() {
        return (Brain<Crawler>) super.getBrain();
    }

    @Override
    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean bl = super.damage(source, amount);
        if (!this.world.isClient && !this.isAiDisabled()) {
            Entity entity = source.getAttacker();
            if (this.brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && entity instanceof LivingEntity livingEntity && (!(source instanceof ProjectileDamageSource) || this.isInRange(livingEntity, 5.0))) {
                this.updateAttackTarget(livingEntity);
            }
        }

        return bl;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_ATTACK_SOUND) {
            //this.playSound(RegisterSounds.ENTITY_CRAWLER_ATTACK, 1.0F, this.getSoundPitch());
        } else {
            super.handleStatus(status);
        }
    }

    public void updateAttackTarget(LivingEntity target) {
        UpdateAttackTargetTask.updateAttackTarget(this, target);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        //return this.world.isClient ? null : RegisterSounds.ENTITY_CRAWLER_AMBIENT;
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        //return RegisterSounds.ENTITY_CRAWLER_HURT;
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        //return RegisterSounds.ENTITY_CRAWLER_DEATH;
        return super.getDeathSound();
    }

    @Override
    protected SoundEvent getSwimSound() {
        //return RegisterSounds.ENTITY_CRAWLER_SWIM;
        return super.getSwimSound();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        //this.playSound(SoundEvents.ENTITY_CRAWLER_STEP, 1.0F, 1.0F);
        super.playStepSound(pos, state);
    }
}
