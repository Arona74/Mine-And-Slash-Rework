package com.robertx22.age_of_exile.aoe_data.database.unique_gears;

import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.armor.ArmorRunewords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.armor.BootsRunewords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.armor.HelmetRunewords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.armor.PantRunewords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.jewelry.NecklaceRunewords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.jewelry.RingRuneWords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.weapon.BowRuneWords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.weapon.StaffRuneWords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.runewords.weapon.SwordRunewords;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.armor.BootsUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.armor.ChestUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.armor.HelmetUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.armor.PantsUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.jewelry.UniqueNecklaces;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.jewelry.UniqueRings;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.offhand.ShieldUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.weapon.BowUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.weapon.StaffUniques;
import com.robertx22.age_of_exile.aoe_data.database.unique_gears.uniques.weapon.SwordUniques;
import com.robertx22.library_of_exile.registry.ExileRegistryInit;

public class UniqueGearReg implements ExileRegistryInit {

    @Override
    public void registerAll() {

        new HelmetUniques().registerAll();
        new ChestUniques().registerAll();
        new PantsUniques().registerAll();
        new BootsUniques().registerAll();

        new ShieldUniques().registerAll();

        new UniqueRings().registerAll();
        new UniqueNecklaces().registerAll();

        new StaffUniques().registerAll();
        new BowUniques().registerAll();
        new SwordUniques().registerAll();

        new NecklaceRunewords().registerAll();
        new RingRuneWords().registerAll();

        new SwordRunewords().registerAll();
        new StaffRuneWords().registerAll();
        new BowRuneWords().registerAll();

        new HelmetRunewords().registerAll();
        new ArmorRunewords().registerAll();
        new BootsRunewords().registerAll();
        new PantRunewords().registerAll();


    }
}
