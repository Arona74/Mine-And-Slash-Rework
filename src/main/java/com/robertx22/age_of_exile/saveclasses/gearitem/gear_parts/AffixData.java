package com.robertx22.age_of_exile.saveclasses.gearitem.gear_parts;

import com.robertx22.age_of_exile.database.Weighted;
import com.robertx22.age_of_exile.database.data.MinMax;
import com.robertx22.age_of_exile.database.data.affixes.Affix;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.ExactStatData;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.IRerollable;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.IStatsContainer;
import com.robertx22.age_of_exile.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.saveclasses.item_classes.tooltips.TooltipStatInfo;
import com.robertx22.age_of_exile.saveclasses.item_classes.tooltips.TooltipStatWithContext;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.IRarity;
import com.robertx22.library_of_exile.registry.FilterListWrap;
import com.robertx22.library_of_exile.utils.RandomUtils;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class AffixData implements IRerollable, IStatsContainer {


    // perc
    public Integer p = -1;
    public String id;
    // tier
    public String rar = IRarity.COMMON_ID;
    public Affix.Type ty;


    public GearRarity getRarity() {
        return ExileDB.GearRarities().get(rar);
    }


    public void upgradeRarity() {

        var r = getRarity();

        if (r.hasHigherRarity()) {
            this.rar = r.getHigherRarity().GUID();
        }

        RerollNumbers();
    }

    public void setMaxRarity() {

        var r = getRarity();

        if (r.hasHigherRarity()) {
            this.rar = r.getHigherRarity().GUID();
        }

        RerollNumbers();
    }

    public void downgradeRarity() {

        var r = getRarity();

        Optional<GearRarity> opt = ExileDB.GearRarities().getList().stream().filter(x -> x.getHigherRarity() == r).findAny();

        if (opt.isPresent()) {
            this.rar = opt.get().GUID();
        }
        RerollNumbers();
    }


    public MinMax getMinMax() {
        return getRarity().stat_percents;
    }

    public AffixData(Affix.Type type) {
        this.ty = type;
    }


    private AffixData() {
    }

    public boolean isEmpty() {
        return p < 0;
    }

    public Affix.Type getAffixType() {
        return ty;
    }


    public List<Component> GetTooltipString(TooltipInfo info, int lvl) {
        List<Component> list = new ArrayList<Component>();
        getAllStatsWithCtx(lvl, info).forEach(x -> list.addAll(x.GetTooltipString(info)));
        return list;
    }

    public Affix getAffix() {
        return ExileDB.Affixes()
                .get(this.id);
    }


    @Override
    public void RerollNumbers(GearItemData gear) {
        RerollNumbers();
    }

    public void RerollNumbers() {
        p = getMinMax().random();
    }

    public final Affix BaseAffix() {
        return ExileDB.Affixes()
                .get(id);
    }

    public List<TooltipStatWithContext> getAllStatsWithCtx(int lvl, TooltipInfo info) {
        List<TooltipStatWithContext> list = new ArrayList<>();
        this.BaseAffix()
                .getStats()
                .forEach(x -> {
                    ExactStatData exact = x.ToExactStat(p, lvl);
                    TooltipStatInfo confo = new TooltipStatInfo(exact, p, info);
                    confo.affix_rarity = this.getRarity();
                    list.add(new TooltipStatWithContext(confo, x, (int) lvl));
                });
        return list;
    }

    public boolean isValid() {
        if (!ExileDB.Affixes()
                .isRegistered(this.id)) {
            return false;
        }
        if (this.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public List<ExactStatData> GetAllStats(GearItemData gear) {

        return GetAllStats(gear.getLevel());

    }

    public List<ExactStatData> GetAllStats(int lvl) {

        if (!isValid()) {
            return Arrays.asList();
        }

        return this.BaseAffix()
                .getStats()
                .stream()
                .map(x -> x.ToExactStat(p, lvl))
                .collect(Collectors.toList());

    }

    public void create(GearItemData gear, Affix suffix) {
        id = suffix.GUID();
        RerollNumbers(gear);
    }

    @Override
    public void RerollFully(GearItemData gear) {

        Affix affix = null;
        try {

            FilterListWrap<Affix> list = ExileDB.Affixes()
                    .getFilterWrapped(x -> x.type == getAffixType() && gear.canGetAffix(x));

            if (list.list.isEmpty()) {
                System.out.print("Gear Type: " + gear.gtype + " affixtype: " + this.ty.name());
            }

            affix = list
                    .random();

            this.randomizeTier(gear.getRarity());

        } catch (Exception e) {
            System.out.print("Gear Type: " + gear.gtype + " affixtype: " + this.ty.name());
            e.printStackTrace();
        }

        this.create(gear, affix);

    }


    // this is kinda simplified.. but might be fine


    public void randomizeTier(GearRarity rar) {

        // we use special weight so it can be further customized
        var list = ExileDB.GearRarities()
                .getFilterWrapped(x -> !x.is_unique_item && rar.item_tier >= x.item_tier).list.stream()
                .map(x -> new Weighted<GearRarity>(x, x.affix_rarity_weight)).toList();

        this.rar = RandomUtils.weightedRandom(list).obj.GUID();


    }
}
