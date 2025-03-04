package com.robertx22.age_of_exile.loot;

import com.robertx22.age_of_exile.capability.player.data.PlayerConfigData;
import com.robertx22.age_of_exile.database.data.rarities.GearRarity;
import com.robertx22.age_of_exile.loot.generators.*;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.datasaving.StackSaving;
import com.robertx22.age_of_exile.uncommon.localization.Words;
import com.robertx22.age_of_exile.uncommon.utilityclasses.ItemUtils;
import com.robertx22.library_of_exile.utils.RandomUtils;
import com.robertx22.library_of_exile.utils.SoundUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MasterLootGen {

    public static List<ItemStack> generateLoot(LootInfo info) {
        List<ItemStack> items = new ArrayList<>();
        try {

            if (info == null) {
                return items;
            }

            items = populateOnce(info);

            int tries = 0;

            while (items.size() < info.getMinItems()) {

                tries++;
                if (tries > 20) {
                    System.out.println("Tried to generate loot many times but failed! " + info.toString());
                    break;
                }
                List<ItemStack> extra = populateOnce(info);
                if (!extra.isEmpty()) {
                    items.add(RandomUtils.randomFromList(extra));
                }
            }

            tries = 0;

            while (items.size() > info.getMaxItems()) {
                tries++;
                if (tries > 50) {
                    System.out.println("Took too many tries to remove items from masterlootgen");
                    break;
                }
                items.remove(RandomUtils.RandomRange(0, items.size() - 1));
            }


            items.forEach(x -> {
                ItemUtils.tryAnnounceItem(x, info.player);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }


    private static List<ItemStack> populateOnce(LootInfo info) {
        List<ItemStack> items = new ArrayList<ItemStack>();

        if (info == null) {
            return items;
        }

        try {
            items.addAll(new GearLootGen(info).tryGenerate());
            items.addAll(new SoulLootGen(info).tryGenerate());
            items.addAll(new UberFragLootGen(info).tryGenerate());

            items.addAll(new AuraGemLootGen(info).tryGenerate());
            items.addAll(new SuppGemLootGen(info).tryGenerate());

            items.addAll(new JewelLootGen(info).tryGenerate());

            items.addAll(new CurrencyLootGen(info).tryGenerate());
            items.addAll(new MapLootGen(info).tryGenerate());
            items.addAll(new GemLootGen(info).tryGenerate());
            items.addAll(new RuneLootGen(info).tryGenerate());
            items.addAll(new LootChestGen(info).tryGenerate());

            items.addAll(new WatcherEyeLootGen(info).tryGenerate());


        } catch (Exception e) {
            e.printStackTrace();
        }


        return items.stream()
                .filter(x -> x != null && !x.isEmpty())
                .collect(Collectors.toList());
    }

    public static List<ItemStack> generateLoot(LivingEntity victim, Player killer) {

        LootInfo info = LootInfo.ofMobKilled(killer, victim);
        info.gatherLootMultipliers();

        List<ItemStack> items = generateLoot(info);

        if (Load.player(killer).config.isConfigEnabled(PlayerConfigData.Config.MOB_DEATH_MESSAGES)) {

            var hovertext = Component.empty();
            hovertext.append(Words.LOOT_MODIFIERS_INFO.locName().withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD).append("\n"));

            for (LootModifier mod : info.lootMods) {
                hovertext.append(mod.name.getFullName(mod.multi)).append("\n");
            }

            var hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hovertext);

            var msg = Words.MOB_KILL_LOOT_INFO_MSG.locName(victim.getDisplayName()).withStyle(ChatFormatting.GREEN);
            msg = msg.append(Words.MOB_DROPS_INFO.locName(items.size()).withStyle(Style.EMPTY.withHoverEvent(hover).applyFormats(ChatFormatting.YELLOW)));
            killer.sendSystemMessage(msg);
        }

        return items;
    }

    public static void genAndDrop(LivingEntity victim, Player killer) {
        List<ItemStack> items = generateLoot(victim, killer);
        for (ItemStack stack : items) {


            if (StackSaving.GEARS.has(stack)) {
                GearRarity rar = StackSaving.GEARS.loadFrom(stack)
                        .getRarity();
                if (rar.is_unique_item) {
                    SoundUtils.ding(victim.level(), victim.blockPosition());
                }
            }

            victim.spawnAtLocation(stack, 1F);
        }
    }

}
