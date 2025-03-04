package com.robertx22.age_of_exile.loot.generators;

import com.robertx22.age_of_exile.config.forge.ServerContainer;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.loot.LootInfo;
import com.robertx22.age_of_exile.loot.blueprints.GearBlueprint;
import com.robertx22.age_of_exile.uncommon.enumclasses.LootType;
import net.minecraft.world.item.ItemStack;

public class GemLootGen extends BaseLootGen<GearBlueprint> {

    public GemLootGen(LootInfo info) {
        super(info);

    }

    @Override
    public float baseDropChance() {
        return (float) (ServerContainer.get().GEM_DROPRATE.get().floatValue());
    }

    @Override
    public LootType lootType() {
        return LootType.Gem;
    }

    @Override
    public boolean condition() {
        return !ExileDB.Gems().getFilterWrapped(x -> this.info.level >= x.getReqLevelToDrop()).list.isEmpty();
    }

    @Override
    public ItemStack generateOne() {
        return ExileDB.Gems().getFilterWrapped(x -> this.info.level >= x.getReqLevelToDrop()).random().getItem().getDefaultInstance();
    }

}
