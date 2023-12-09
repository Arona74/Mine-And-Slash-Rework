package com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases;

import com.robertx22.age_of_exile.aoe_data.database.stats.old.DatapackStats;
import com.robertx22.age_of_exile.capability.entity.EntityData;
import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.database.data.stats.StatScaling;
import com.robertx22.age_of_exile.database.data.stats.types.core_stats.AllAttributes;
import com.robertx22.age_of_exile.uncommon.enumclasses.PlayStyle;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatRequirement {


    public static String CHECK_YES_ICON = "\u2714"; // they removed the damn icons..
    public static String NO_ICON = "\u274C";

    public static StatRequirement EMPTY = new StatRequirement();

    //public HashMap<String, Float> base_req = new HashMap<>();
    public HashMap<String, Float> scaling_req = new HashMap<>();

    public StatRequirement(StatRequirement r) {
        //this.base_req = new HashMap<>(r.base_req);
        this.scaling_req = new HashMap<>(r.scaling_req);
    }

    public static StatRequirement of(PlayStyle... styles) {
        StatRequirement r = new StatRequirement();
        float multi = 0.75F / (float) styles.length;

        for (PlayStyle style : styles) {
            r.setStyleReq(style, multi);
        }

        return r;
    }

    public static StatRequirement combine(StatRequirement one, StatRequirement two) {
        StatRequirement req = new StatRequirement(one);

        two.scaling_req.entrySet()
                .forEach(x -> {
                    req.scaling_req.put(x.getKey(), req.scaling_req.getOrDefault(x.getKey(), 0F) + x.getValue());
                });

        return req;
    }

    public StatRequirement() {
    }

    public boolean meetsReq(int lvl, EntityData data) {

        for (Stat x : AllAttributes.getInstance()
                .coreStatsThatBenefit()) {

            int num = getReq(x, lvl);

            if (num > data.getUnit().getCalculatedStat(x).getValue()) {
                return false;
            }

        }

        return true;

    }

    private int getReq(Stat stat, int lvl) {
        return (int) StatScaling.STAT_REQ.scale(scaling_req.getOrDefault(stat.GUID(), 0F), lvl);
    }

    public StatRequirement setStyleReq(PlayStyle style, float req) {
        this.scaling_req.put(style.getStat().GUID(), req);
        return this;
    }

    public StatRequirement setDex(float req) {
        this.scaling_req.put(DatapackStats.DEX.GUID(), req);
        return this;
    }

    public StatRequirement setInt(float req) {
        this.scaling_req.put(DatapackStats.INT.GUID(), req);
        return this;
    }

    public StatRequirement setStr(float req) {
        this.scaling_req.put(DatapackStats.STR.GUID(), req);
        return this;
    }


    public List<Component> GetTooltipString(int lvl, EntityData data) {
        List<Component> list = new ArrayList<>();

        for (Stat x : AllAttributes.getInstance()
                .coreStatsThatBenefit()) {

            int num = getReq(x, lvl);

            if (num > 0) {
                list.add(getTooltip(num, x, data));
            }

        }

        return list;
    }

    public boolean isEmpty() {
        return this.scaling_req.isEmpty();
    }


    static Component getTooltip(int req, Stat stat, EntityData data) {

        if (data.getUnit()
                .getCalculatedStat(stat)
                .getValue() >= req) {
            return Component.literal(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + CHECK_YES_ICON + " ").append(Words.Stat_Req.locName(stat.locName())
                            .withStyle(ChatFormatting.GRAY))
                    .append("" + ChatFormatting.GRAY + req + " ");
        } else {

            return Component.literal(ChatFormatting.RED + "" + ChatFormatting.BOLD + NO_ICON + " ").append(Words.Stat_Req.locName(stat.locName())
                            .withStyle(ChatFormatting.GRAY))
                    .append("" + ChatFormatting.GRAY + req + " ");

        }

    }


}
