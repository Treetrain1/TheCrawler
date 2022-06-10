package net.frozenblock.crawler.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.crawler.TheCrawler;
import net.frozenblock.crawler.TheCrawlerClient;
import net.frozenblock.crawler.entity.Crawler;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class CrawlerRenderer extends MobEntityRenderer<Crawler, CrawlerModel<Crawler>> {

    private static final Identifier TEXTURE = new Identifier(TheCrawler.MOD_ID, "textures/entity/crawler/crawler.png");

    public CrawlerRenderer(EntityRendererFactory.Context context) {
        super(context, new CrawlerModel<>(context.getPart(TheCrawlerClient.CRAWLER)), 0.9F);
    }

    public Identifier getTexture(Crawler crawler) {
        return TEXTURE;
    }
}
