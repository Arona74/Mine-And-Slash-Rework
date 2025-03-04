package com.robertx22.age_of_exile.loot.blueprints.bases;

import com.robertx22.age_of_exile.database.data.gear_slots.GearSlot;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.loot.blueprints.GearBlueprint;

public class GearTypePart extends BlueprintPart<BaseGearType, GearBlueprint> {

    public GearTypePart(GearBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    protected BaseGearType generateIfNull() {

        GearSlot slot = ExileDB.GearSlots()
                .random();


        return ExileDB.GearTypes()
                .getFilterWrapped(x -> x.getGearSlot()
                        .GUID()
                        .equals(slot.GUID()))
                .random();
    }

    public void set(String id) {
        super.set(ExileDB.GearTypes()
                .get(id));
    }

}


