package com.robertx22.age_of_exile.uncommon.effectdatas.rework.condition;

import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.saveclasses.unit.StatData;
import com.robertx22.age_of_exile.uncommon.effectdatas.EffectEvent;
import com.robertx22.age_of_exile.uncommon.enumclasses.WeaponTypes;
import com.robertx22.age_of_exile.uncommon.interfaces.EffectSides;

public class WeaponTypeMatches extends StatCondition {

    public String type;

    public WeaponTypeMatches(WeaponTypes type) {
        super("is_" + type.id + "_wep_type", "wep_type_match");
        this.type = type.id;
    }

    public WeaponTypeMatches() {
        super("", "wep_type_match");
    }

    @Override
    public boolean can(EffectEvent event, EffectSides statSource, StatData data, Stat stat) {
        var wep = event.data.getWeaponType();
        return wep.id.equals(type);
    }

    @Override
    public Class<? extends StatCondition> getSerClass() {
        return WeaponTypeMatches.class;
    }

}

