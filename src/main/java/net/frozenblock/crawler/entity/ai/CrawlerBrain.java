package net.frozenblock.crawler.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.frozenblock.crawler.entity.CrawlerEntity;
import net.frozenblock.crawler.entity.ai.task.CrawlerDigTask;
import net.frozenblock.crawler.entity.ai.task.CrawlerEmergeTask;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.Unit;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

public class CrawlerBrain {

    private static final int DIG_DURATION = MathHelper.ceil(50.0F);
    private static final int EMERGE_DURATION = MathHelper.ceil(133.59999F);

    private static final List<SensorType<? extends Sensor<? super CrawlerEntity>>> SENSORS = List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
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

    public static void updateActivities(CrawlerEntity crawler) {
        crawler.getBrain()
                .resetPossibleActivities(
                        ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.FIGHT, Activity.IDLE)
                );
    }

    public static Brain<?> create(CrawlerEntity crawler, Dynamic<?> dynamic) {
        Brain.Profile<CrawlerEntity> profile = Brain.createProfile(MEMORY_MODULES, SENSORS);
        Brain<CrawlerEntity> brain = profile.deserialize(dynamic);
        addCoreActivities(brain);
        addEmergeActivities(brain);
        addDigActivities(brain);
        addIdleActivities(brain);
        addFightActivities(crawler, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<CrawlerEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new LookAroundTask(45, 90), new WanderAroundTask()));
    }

    private static void addEmergeActivities(Brain<CrawlerEntity> brain) {
        brain.setTaskList(Activity.EMERGE, 5, ImmutableList.of(new CrawlerEmergeTask<>(EMERGE_DURATION)), MemoryModuleType.IS_EMERGING);
    }

    private static void addDigActivities(Brain<CrawlerEntity> brain) {
        brain.setTaskList(
                Activity.DIG,
                ImmutableList.of(Pair.of(0, new DismountVehicleTask()), Pair.of(1, new CrawlerDigTask<>(DIG_DURATION))),
                ImmutableSet.of(
                        Pair.of(MemoryModuleType.DIG_COOLDOWN, MemoryModuleState.VALUE_ABSENT)
                )
        );
    }

    private static void addCrawlActivities(Brain<CrawlerEntity> brain) {

    }

    private static void addIdleActivities(Brain<CrawlerEntity> brain) {
        brain.setTaskList(Activity.IDLE, 10, ImmutableList.of(new UpdateAttackTargetTask<>(CrawlerEntity::getCrawlerTarget), new TimeLimitedTask<>(new FollowMobTask(16.0F), UniformIntProvider.create(30, 60)), new RandomTask<>(ImmutableList.of(Pair.of(new StrollTask(0.4F), 2), Pair.of(new GoTowardsLookTarget(0.4F, 3), 2), Pair.of(new WaitTask(30, 60), 1)))));
    }

    private static void addFightActivities(CrawlerEntity crawler, Brain<CrawlerEntity> brain) {
        brain.setTaskList(Activity.FIGHT, 10, ImmutableList.of(new RangedApproachTask(1.0F), new MeleeAttackTask(18), new ForgetAttackTargetTask<>()), MemoryModuleType.ATTACK_TARGET);
    }

    public static void resetDigCooldown(LivingEntity crawler) {
        if (crawler.getBrain().hasMemoryModule(MemoryModuleType.DIG_COOLDOWN)) {
            crawler.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 100L);
        }
    }

}
