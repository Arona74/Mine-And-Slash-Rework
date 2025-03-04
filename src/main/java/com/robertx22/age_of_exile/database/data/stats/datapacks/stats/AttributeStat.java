package com.robertx22.age_of_exile.database.data.stats.datapacks.stats;

import com.robertx22.age_of_exile.aoe_data.database.stats.old.DatapackStats;
import com.robertx22.age_of_exile.database.data.stats.StatScaling;
import com.robertx22.age_of_exile.database.data.stats.datapacks.base.BaseDatapackStat;
import com.robertx22.age_of_exile.saveclasses.unit.StatData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class AttributeStat extends BaseDatapackStat {

    public static String SER_ID = "vanilla_attribute_stat_ser";

    transient String locname;

    public UUID uuid;
    public String attributeId;
    public Attribute attribute;

    public AttributeStat(String id, String locname, UUID uuid, Attribute attribute, boolean perc) {
        super(SER_ID);
        this.id = id;
        this.locname = locname;
        this.uuid = uuid;
        this.attributeId = BuiltInRegistries.ATTRIBUTE.getKey(attribute)
                .toString();
        this.attribute = attribute;
        this.is_perc = perc;
        this.scaling = StatScaling.NONE;
        
        DatapackStats.tryAdd(this);
    }

    @Override
    public String locDescForLangFile() {
        return "Increase vanilla attribute.";
    }

    @Override
    public String locNameForLangFile() {
        return locname;
    }

    public void addToEntity(LivingEntity en, StatData data) {

        float val = data.getValue();

        AttributeModifier.Operation operation = AttributeModifier.Operation.ADDITION;

        if (IsPercent()) {
            operation = AttributeModifier.Operation.MULTIPLY_TOTAL;

            val = (00F + val) / 100F;
        }

        AttributeModifier mod = new AttributeModifier(
                uuid,
                attributeId,
                val,
                operation
        );

        AttributeInstance atri = en.getAttribute(attribute);

        if (atri != null) {
            if (atri.hasModifier(mod)) {
                atri.removeModifier(mod); // KEEP THIS OR UPDATE WONT MAKE HP CORRECT!!!
            }
            atri.addPermanentModifier(mod);
        }

    }
}
