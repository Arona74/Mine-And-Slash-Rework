package com.robertx22.age_of_exile.database.data.stats.types.loot;

import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.uncommon.enumclasses.Elements;
import net.minecraft.ChatFormatting;

public class TreasureQuality extends Stat {

    private TreasureQuality() {
        this.icon = "\u2663";
        this.format = ChatFormatting.AQUA.getName();
        this.max = 150;
    }

    public static TreasureQuality getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean IsPercent() {
        return true;
    }

    @Override
    public Elements getElement() {
        return null;
    }

    @Override
    public String locDescForLangFile() {
        return "Adds chance for higher rarity of items found.";
    }

    @Override
    public String GUID() {
        return "magic_find";
    }

    @Override
    public String locNameForLangFile() {
        return "Magic Find";
    }

    private static class SingletonHolder {
        private static final TreasureQuality INSTANCE = new TreasureQuality();
    }
}
