package net.frozenblock.crawler.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.frozenblock.crawler.TheCrawler;
import net.frozenblock.crawler.entity.Crawler;
import net.frozenblock.crawler.entity.ai.CrawlerBrain;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterEntities {

    public static final EntityType<Crawler> CRAWLER = Registry.register(Registry.ENTITY_TYPE, new Identifier(TheCrawler.MOD_ID, "crawler"), FabricEntityTypeBuilder.createMob().spawnGroup(SpawnGroup.MONSTER).entityFactory(Crawler::new).dimensions(EntityDimensions.fixed(1.7F, 2.8F)).trackRangeBlocks(16).fireImmune().defaultAttributes(Crawler::addAttributes).build());

    public static void init() {

    }
}
