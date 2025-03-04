package com.robertx22.age_of_exile.database.data.currency.gear;

import com.robertx22.age_of_exile.database.data.currency.base.GearCurrency;
import com.robertx22.age_of_exile.database.data.currency.base.GearOutcome;
import com.robertx22.age_of_exile.database.data.currency.loc_reqs.LocReqContext;
import com.robertx22.age_of_exile.database.data.profession.ExplainedResult;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.localization.Chats;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.temp.SkillItemTier;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class SharpeningStone extends GearCurrency {

    SkillItemTier skill;
    int tier;
    public int amount;

    public SharpeningStone(SkillItemTier skill) {
        this.skill = skill;
        this.amount = (skill.tier + 1) * 2;
        this.tier = skill.tier;
    }

    @Override
    public List<GearOutcome> getOutcomes() {

        return Arrays.asList(
                new GearOutcome() {
                    @Override
                    public Words getName() {
                        return Words.UpgradeQuality;
                    }

                    @Override
                    public OutcomeType getOutcomeType() {
                        return OutcomeType.GOOD;
                    }

                    @Override
                    public ItemStack modify(LocReqContext ctx, GearItemData data, ItemStack stack) {
                        data.data.set(GearItemData.KEYS.USED_SHARPENING_STONE, true);
                        data.setQuality(data.getQuality() + amount);
                        StackSaving.GEARS.saveTo(stack, data);
                        return stack;
                    }

                    @Override
                    public int Weight() {
                        return 1000;
                    }
                }
        );
    }


    @Override
    public int getPotentialLoss() {
        return 0;
    }

    @Override
    public ExplainedResult canBeModified(GearItemData data) {
        if (!data.data.get(GearItemData.KEYS.USED_SHARPENING_STONE)) {
            return ExplainedResult.success();
        }
        return ExplainedResult.failure(Chats.THIS_ITEM_CANT_BE_USED_MORE_THAN_X_TIMES.locName(1));
    }

    @Override
    public String locDescForLangFile() {
        return "Upgrades Quality of An Item by " + amount + "%" + ", can only be used once";
    }

    @Override
    public String locNameForLangFile() {
        return skill.word + " Sharpening Stone";
    }

    @Override
    public String GUID() {
        return "sharpening_stone" + tier;
    }

    @Override
    public int Weight() {
        return 0; // no drop, only craftable
    }
}
