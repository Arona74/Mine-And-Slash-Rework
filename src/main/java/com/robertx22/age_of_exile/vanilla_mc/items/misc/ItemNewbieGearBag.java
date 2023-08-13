package com.robertx22.age_of_exile.vanilla_mc.items.misc;

import com.robertx22.age_of_exile.aoe_data.database.gear_slots.GearSlots;
import com.robertx22.age_of_exile.aoe_data.database.spells.schools.BasicAttackSpells;
import com.robertx22.age_of_exile.aoe_data.database.spells.schools.RangerSpells;
import com.robertx22.age_of_exile.aoe_data.database.spells.schools.WaterSpells;
import com.robertx22.age_of_exile.database.data.gear_types.bases.BaseGearType;
import com.robertx22.age_of_exile.database.data.perks.Perk;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.loot.LootInfo;
import com.robertx22.age_of_exile.loot.blueprints.GearBlueprint;
import com.robertx22.age_of_exile.loot.blueprints.SkillGemBlueprint;
import com.robertx22.age_of_exile.saveclasses.item_classes.GearItemData;
import com.robertx22.age_of_exile.saveclasses.skill_gem.SkillGemData;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.interfaces.data_items.IRarity;
import com.robertx22.age_of_exile.uncommon.utilityclasses.PlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemNewbieGearBag extends Item {

    public ItemNewbieGearBag() {
        super(new Properties());

    }


    static HashMap<String, NewbieContent> MAP = new HashMap<>();
    static NewbieContent defaultContent = new NewbieContent(Arrays.asList(GearSlots.STAFF, GearSlots.SWORD, GearSlots.BOW));
    static List<String> spells = Arrays.asList(BasicAttackSpells.FIREBALL_ID, WaterSpells.TIDAL_STRIKE, RangerSpells.CHARGED_BOLT);

    static {
    }

    static void giveNewbieItemsFor(Player player, Perk perk) {

        NewbieContent c = defaultContent;

        if (MAP.containsKey(perk.GUID())) {
            c = MAP.get(perk.GUID());
        }

        c.give(player);

    }

    static class NewbieContent {

        public List<String> gearslots;

        public List<ItemStack> stacks = new ArrayList<>();

        public NewbieContent(List<String> gearslots) {
            this.gearslots = gearslots;

        }

        public NewbieContent addStack(ItemStack stack) {
            this.stacks.add(stack);
            return this;
        }

        
        public void give(Player player) {

            // todo if i create a choose gui, give players a choice of 1 spell
            // items.forEach(x -> PlayerUtils.giveItem(new ItemStack(x.get()), player));
            for (String s : spells) {
                SkillGemBlueprint b = new SkillGemBlueprint(LootInfo.ofLevel(1), SkillGemData.SkillGemType.SKILL);
                b.level.set(1);

                var data = b.createData();
                data.id = s;

                ItemStack stack = data.getItem().getDefaultInstance();
                StackSaving.SKILL_GEM.saveTo(stack, data);

                PlayerUtils.giveItem(stack, player);
            }


            gearslots.forEach(x -> {
                BaseGearType gear = ExileDB.GearTypes()
                        .getFilterWrapped(e -> e.gear_slot.equals(x))
                        .random();
                GearItemData data = getBlueprint(gear).createData();
                data.lvl = 1;
                data.sal = false;

                GearBlueprint b = new GearBlueprint(1);
                b.level.set(1);
                b.rarity.set(ExileDB.GearRarities()
                        .get(IRarity.COMMON_ID));
                b.gearItemSlot.set(gear);

                ItemStack stack = b.createStack();

                EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(Enchantments.UNBREAKING, 3));

                PlayerUtils.giveItem(stack, player);

            });

            stacks.forEach(x -> PlayerUtils.giveItem(x, player));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

        if (!worldIn.isClientSide) {
            try {

                List<Perk> starts = Load.playerRPGData(playerIn).talents
                        .getAllAllocatedPerks()
                        .values()
                        .stream()
                        .filter(x -> x.is_entry)
                        .collect(Collectors.toList());

                if (true || !starts.isEmpty()) { // todo

                    defaultContent.give(playerIn);
                    // ItemNewbieGearBag.giveNewbieItemsFor(playerIn, starts.get(0));

                    if (ModList.get()
                            .isLoaded("patchouli")) {
                        /*
                        // dont give till i update it
                        // guide book
                        ItemStack book = new ItemStack(VanillaUTIL.REGISTRY.items().get(new Identifier("patchouli", "guide_book")));
                        CompoundTag tag = new CompoundTag();
                        tag.putString("patchouli:book", "mmorpg:age_of_exile_guide");
                        book.setTag(tag);
                        PlayerUtils.giveItem(book, playerIn);

                         */
                    }

                    playerIn.getItemInHand(handIn)
                            .shrink(1);

                } else {
                    playerIn.displayClientMessage(Component.literal("Choose your path to open this. (Press [H] and then open Talent Tree scren"), false);
                }

                return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, playerIn.getItemInHand(handIn));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
    }

    private static GearBlueprint getBlueprint(BaseGearType type) {
        GearBlueprint print = new GearBlueprint(1);        //TODO
        print.gearItemSlot.set(type);
        print.rarity.set(ExileDB.GearRarities()
                .get(IRarity.COMMON_ID));
        return print;
    }

}
