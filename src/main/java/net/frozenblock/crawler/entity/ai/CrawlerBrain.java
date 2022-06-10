package net.frozenblock.crawler.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.frozenblock.crawler.entity.Crawler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;
import java.util.Optional;

public class CrawlerBrain {

    private static final List<SensorType<? extends Sensor<? super Crawler>>> SENSORS = List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
    private static final List<MemoryModuleType<?>> MEMORY_MODULES = List.of(
            MemoryModuleType.MOBS,
            MemoryModuleType.VISIBLE_MOBS,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN
    );

    public static void updateActivities(WardenEntity warden) {
        warden.getBrain()
                .resetPossibleActivities(
                        ImmutableList.of(Activity.FIGHT, Activity.IDLE)
                );
    }

    public static Brain<?> create(Crawler crawler, Dynamic<?> dynamic) {
        Brain.Profile<Crawler> profile = Brain.createProfile(MEMORY_MODULES, SENSORS);
        Brain<Crawler> brain = profile.deserialize(dynamic);
        addCoreActivities(brain);
        addIdleActivities(brain);
        addFightActivities(crawler, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<Crawler> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new LookAroundTask(45, 90), new WanderAroundTask()));
    }

    private static void addIdleActivities(Brain<Crawler> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(new UpdateAttackTargetTask<>(Crawler::getCrawlerTarget), new TimeLimitedTask<>(new FollowMobTask(16.0F), UniformIntProvider.create(30, 60)), new RandomTask<>(ImmutableList.of(Pair.of(new StrollTask(0.4F), 2), Pair.of(new GoTowardsLookTarget(0.4F, 3), 2), Pair.of(new WaitTask(30, 60), 1)))));
    }

    private static void addFightActivities(Crawler crawler, Brain<Crawler> brain) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of(new RangedApproachTask(1.2F), new MeleeAttackTask(18), new ForgetAttackTargetTask<>()), MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean isTargeting(Crawler crawler, LivingEntity entity) {
        return crawler.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).filter(entityx -> entityx == entity).isPresent();
    }

}
