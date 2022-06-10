package net.frozenblock.crawler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.frozenblock.crawler.entity.render.CrawlerModel;
import net.frozenblock.crawler.entity.render.CrawlerRenderer;
import net.frozenblock.crawler.registry.RegisterEntities;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TheCrawlerClient implements ClientModInitializer {

    public static final EntityModelLayer CRAWLER = new EntityModelLayer(new Identifier(TheCrawler.MOD_ID, "crawler"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(RegisterEntities.CRAWLER, CrawlerRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(CRAWLER, CrawlerModel::getTexturedModelData);
    }
}
