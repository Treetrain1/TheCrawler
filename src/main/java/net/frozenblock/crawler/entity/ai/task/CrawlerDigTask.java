package net.frozenblock.crawler.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import net.frozenblock.crawler.entity.CrawlerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

public class CrawlerDigTask<E extends CrawlerEntity> extends Task<E> {
    public CrawlerDigTask(int duration) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT), duration);
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, E crawler, long l) {
        return crawler.getRemovalReason() == null;
    }

    protected boolean shouldRun(ServerWorld serverWorld, E crawler) {
        return crawler.isOnGround() || crawler.isTouchingWater() || crawler.isInLava();
    }

    protected void run(ServerWorld serverWorld, E crawler, long l) {
        if (crawler.isOnGround()) {
            crawler.setPose(EntityPose.DIGGING);
            crawler.playSound(SoundEvents.ENTITY_WARDEN_DIG, 5.0F, 1.0F);
        } else {
            crawler.playSound(SoundEvents.ENTITY_WARDEN_AGITATED, 5.0F, 1.0F);
            this.finishRunning(serverWorld, crawler, l);
        }

    }

    protected void finishRunning(ServerWorld serverWorld, E crawler, long l) {
        if (crawler.getRemovalReason() == null) {
            crawler.remove(Entity.RemovalReason.DISCARDED);
        }

    }
}
