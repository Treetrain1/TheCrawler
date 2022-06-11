package net.frozenblock.crawler.entity.ai.task;

import com.google.common.collect.ImmutableMap;
import net.frozenblock.crawler.entity.CrawlerEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

public class CrawlerEmergeTask<E extends CrawlerEntity> extends Task<E> {
    public CrawlerEmergeTask(int duration) {
        super(
            ImmutableMap.of(
                MemoryModuleType.IS_EMERGING,
                MemoryModuleState.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET,
                MemoryModuleState.VALUE_ABSENT,
                MemoryModuleType.LOOK_TARGET,
                MemoryModuleState.REGISTERED
            ),
            duration
        );
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, E crawler, long l) {
        return true;
    }

    protected void run(ServerWorld serverWorld, E crawler, long l) {
        crawler.setPose(EntityPose.EMERGING);
        crawler.playSound(SoundEvents.ENTITY_WARDEN_EMERGE, 5.0F, 1.0F);
    }

    protected void finishRunning(ServerWorld serverWorld, E crawler, long l) {
        if (crawler.isInPose(EntityPose.EMERGING)) {
            crawler.setPose(EntityPose.STANDING);
        }

    }
}
