package com.robertx22.age_of_exile.vanilla_mc.items.gemrunes;

import com.robertx22.age_of_exile.database.OptScaleExactStat;
import com.robertx22.age_of_exile.database.data.BaseGem;
import com.robertx22.age_of_exile.database.data.StatMod;
import com.robertx22.age_of_exile.database.data.gear_types.bases.SlotFamily;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.TooltipUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGemItem extends Item {
    public BaseGemItem(Properties settings) {
        super(settings);
    }

    public int weight;

    public abstract BaseGem getBaseRuneGem();

    public abstract float getStatValueMulti();

    public abstract List<StatMod> getStatModsForSerialization(SlotFamily family);

    public List<OptScaleExactStat> getStatsForSerialization(SlotFamily family) {

        List<OptScaleExactStat> list = new ArrayList<>();

        float multi = getStatValueMulti();

        this.getStatModsForSerialization(family)
                .forEach(x -> {
                    OptScaleExactStat stat = new OptScaleExactStat(x.max * multi, x.GetStat(), x.getModType());
                    stat.scale_to_lvl = true;
                    list.add(stat);

                });

        return list;
    }

    public List<Component> getBaseTooltip() {
        List<Component> tooltip = new ArrayList<>();

        if (ExileDB.Runes()
                .isEmpty() || ExileDB.Gems()
                .isEmpty() || getBaseRuneGem() == null) {
            return tooltip; // datapacks didnt register yet
        }

        BaseGem gem = getBaseRuneGem();


        TooltipInfo info = new TooltipInfo();

        tooltip.add(Component.literal(""));
        List<OptScaleExactStat> wep = gem.getFor(SlotFamily.Weapon);
        tooltip.add(Words.WEAPON.locName().withStyle(ChatFormatting.RED));
        for (OptScaleExactStat x : wep) {
            tooltip.addAll(x.GetTooltipString(info));
        }

        tooltip.add(Component.literal(""));
        List<OptScaleExactStat> armor = gem.getFor(SlotFamily.Armor);
        tooltip.add(Words.ARMOR.locName().withStyle(ChatFormatting.BLUE));
        for (OptScaleExactStat x : armor) {
            tooltip.addAll(x.GetTooltipString(info));
        }

        tooltip.add(Component.literal(""));
        List<OptScaleExactStat> jewelry = gem.getFor(SlotFamily.Jewelry);
        tooltip.add(Words.JEWERLY.locName().withStyle(ChatFormatting.LIGHT_PURPLE));
        for (OptScaleExactStat x : jewelry) {
            tooltip.addAll(x.GetTooltipString(info));
        }

        tooltip.add(Component.literal(""));
        tooltip.add(TooltipUtils.tier(gem.tier));
        tooltip.add(Words.PressAltForStatInfo.locName().withStyle(ChatFormatting.BLUE));
        return tooltip;
    }

}
