package net.frozenblock.crawler.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.frozenblock.crawler.TheCrawler;
import net.frozenblock.crawler.entity.CrawlerEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class RegisterEntities {

    public static final EntityType<CrawlerEntity> CRAWLER = Registry.register(Registry.ENTITY_TYPE, new Identifier(TheCrawler.MOD_ID, "crawler"), FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(CrawlerEntity::new).dimensions(EntityDimensions.fixed(1.7F, 2.8F)).fireImmune().defaultAttributes(CrawlerEntity::addAttributes).build());

    public static void init() {
        SpawnRestrictionAccessor.callRegister(CRAWLER, SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.MANGROVE_SWAMP),
                SpawnGroup.MONSTER,
                RegisterEntities.CRAWLER, 3, 1, 3);
    }
}
