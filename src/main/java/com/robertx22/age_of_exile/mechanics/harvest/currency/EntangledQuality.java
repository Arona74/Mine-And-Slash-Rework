package com.robertx22.age_of_exile.mechanics.harvest.currency;

import com.robertx22.age_of_exile.database.data.currency.base.GearCurrency;
import com.robertx22.age_of_exile.database.data.currency.base.GearOutcome;
import com.robertx22.age_of_exile.database.data.currency.loc_reqs.LocReqContext;
import com.robertx22.age_of_exile.database.data.league.LeagueMechanics;
import com.robertx22.age_of_exile.database.data.profession.ExplainedResult;
import com.robertx22.age_of_exile.loot.req.DropRequirement;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.localization.Chats;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class EntangledQuality extends GearCurrency {
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
                        data.setQuality(data.getQuality() + RandomUtils.RandomRange(1, 5));
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
    public DropRequirement getDropReq() {
        return DropRequirement.Builder.of().setOnlyDropsInLeague(LeagueMechanics.HARVEST.GUID()).build();
    }

    @Override
    public int getPotentialLoss() {
        return 0;
    }

    @Override
    public ExplainedResult canBeModified(GearItemData data) {
        if (data.getQuality() < 21) {
            return ExplainedResult.success();
        }
        return ExplainedResult.failure(Chats.CANT_GO_ABOVE.locName(21));
    }

    @Override
    public String locDescForLangFile() {
        return "Randomly Upgrades Quality of An Item (1-5%), boosting gear potential";
    }

    @Override
    public String locNameForLangFile() {
        return "Entangled Orb of Quality";
    }

    @Override
    public String GUID() {
        return "entangled_quality";
    }

    @Override
    public int Weight() {
        return 1000;
    }
}
