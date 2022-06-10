package net.frozenblock.crawler.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.frozenblock.crawler.TheCrawler;
import net.frozenblock.crawler.entity.Crawler;
import net.frozenblock.crawler.entity.ai.CrawlerBrain;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;

public class RegisterEntities {

    public static final EntityType<Crawler> CRAWLER = Registry.register(Registry.ENTITY_TYPE, new Identifier(TheCrawler.MOD_ID, "crawler"), FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(Crawler::new).dimensions(EntityDimensions.fixed(1.7F, 2.8F)).fireImmune().defaultAttributes(Crawler::addAttributes).build());

    public static void init() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.MANGROVE_SWAMP),
                SpawnGroup.MONSTER,
                RegisterEntities.CRAWLER, 1, 1, 1);
    }
}
