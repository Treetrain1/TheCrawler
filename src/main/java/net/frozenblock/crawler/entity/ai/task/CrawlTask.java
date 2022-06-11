package net.frozenblock.crawler.entity.ai.task;

import net.frozenblock.crawler.entity.CrawlerEntity;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;

import java.util.Map;

public class CrawlTask<E extends CrawlerEntity> extends Task<E> {

    public CrawlTask(Map<MemoryModuleType<?>, MemoryModuleState> requiredMemoryState) {
        super(requiredMemoryState);
    }

}
