package com.robertx22.age_of_exile.loot.generators;

import com.robertx22.age_of_exile.config.forge.ServerContainer;
import com.robertx22.age_of_exile.loot.LootInfo;
import com.robertx22.age_of_exile.loot.blueprints.GearBlueprint;
import com.robertx22.age_of_exile.loot.blueprints.SkillGemBlueprint;
import com.robertx22.age_of_exile.saveclasses.skill_gem.SkillGemData;
import com.robertx22.age_of_exile.uncommon.enumclasses.LootType;
import net.minecraft.world.item.ItemStack;

public class AuraGemLootGen extends BaseLootGen<GearBlueprint> {

    public AuraGemLootGen(LootInfo info) {
        super(info);

    }

    @Override
    public float baseDropChance() {
        return (float) (ServerContainer.get().AURA_GEM_DROPRATE.get().floatValue());
    }

    @Override
    public LootType lootType() {
        return LootType.SkillGem;
    }

    @Override
    public boolean condition() {
        return info.level > 10;
    }

    @Override
    public ItemStack generateOne() {
        SkillGemBlueprint blueprint = new SkillGemBlueprint(info, SkillGemData.SkillGemType.AURA);
        return blueprint.createStack();
    }

}