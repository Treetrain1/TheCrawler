package net.frozenblock.crawler.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.frozenblock.crawler.TheCrawler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegisterItems {

    public static final Item CRAWLER_SPAWN_EGG = new SpawnEggItem(RegisterEntities.CRAWLER, Integer.parseInt("00a86b", 16), Integer.parseInt("39a78e", 16), new FabricItemSettings().group(ItemGroup.MISC));

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(TheCrawler.MOD_ID, "crawler_spawn_egg"), CRAWLER_SPAWN_EGG);
    }

}
