package com.robertx22.age_of_exile.database.data.profession.items;

import com.robertx22.age_of_exile.aoe_data.datapacks.models.ItemModelManager;
import com.robertx22.age_of_exile.aoe_data.datapacks.models.ModelHelper;
import com.robertx22.age_of_exile.database.data.gear_types.bases.SlotFamily;
import com.robertx22.age_of_exile.database.data.profession.ICreativeTabTiered;
import com.robertx22.age_of_exile.database.data.profession.LeveledItem;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.mmorpg.SlashRef;
import com.robertx22.age_of_exile.saveclasses.stat_soul.StatSoulData;
import com.robertx22.age_of_exile.uncommon.interfaces.IRarityItem;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.ClientOnly;
import com.robertx22.age_of_exile.uncommon.utilityclasses.StringUTIL;
import com.robertx22.age_of_exile.vanilla_mc.items.misc.AutoItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CraftedSoulItem extends AutoItem implements ICreativeTabTiered, IRarityItem {

    SlotFamily fam;
    String rar;

    public CraftedSoulItem(SlotFamily fam, String rar) {
        super(new Properties());
        this.fam = fam;
        this.rar = rar;
    }

    @Override
    public void generateModel(ItemModelManager manager) {


        new ModelHelper(this, ModelHelper.Type.GENERATED, SlashRef.id("item/stat_soul/family/" + fam.id).toString()).generate();
    }

    public StatSoulData getSoul(ItemStack stack) {
        StatSoulData data = StatSoulData.ofFamily(ExileDB.GearRarities().get(rar), LeveledItem.getTier(stack), fam);

        String force = stack.getOrCreateTag().getString("force_tag");
        if (!force.isEmpty()) {
            data.force_tag = force;
        }
        return data;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> list, TooltipFlag pIsAdvanced) {
        var soul = getSoul(pStack);

        if (soul != null) {
            for (Component c : soul.getTooltip(pStack, false)) {
                list.add(c);
            }

            Player p = ClientOnly.getPlayer();
            if (p != null && p.isCreative()) {
                list.add(Words.DRAG_NO_WORK_CREATIVE.locName().withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            }
        }
    }

    @Override
    public Item getThis() {
        return this;
    }

    @Override
    public String locNameForLangFile() {
        return ExileDB.GearRarities().get(rar).textFormatting() + "Crafted " + StringUTIL.capitalise(rar) + " " + StringUTIL.capitalise(fam.id) + " Soul";
    }

    @Override
    public String GUID() {
        return null;
    }

    @Override
    public GearRarity getItemRarity(ItemStack stack) {
        return ExileDB.GearRarities().get(rar);
    }
}
