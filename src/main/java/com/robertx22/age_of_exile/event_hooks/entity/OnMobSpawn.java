package com.robertx22.age_of_exile.event_hooks.entity;

import com.robertx22.age_of_exile.capability.entity.EntityData;
import com.robertx22.age_of_exile.config.forge.ServerContainer;
import com.robertx22.age_of_exile.database.data.EntityConfig;
import com.robertx22.age_of_exile.database.data.rarities.MobRarity;
import com.robertx22.age_of_exile.database.data.spells.summons.entity.SummonEntity;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.age_of_exile.saveclasses.unit.Unit;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import com.robertx22.age_of_exile.uncommon.utilityclasses.AllyOrEnemy;
import com.robertx22.age_of_exile.uncommon.utilityclasses.EntityFinder;
import com.robertx22.age_of_exile.uncommon.utilityclasses.PlayerUtils;
import com.robertx22.age_of_exile.uncommon.utilityclasses.WorldUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class OnMobSpawn {

    public static void onLoad(Entity entity) {

        if (!(entity instanceof LivingEntity)) {
            return;
        }
        if (entity instanceof Player) {
            return;
        }

        if (WorldUtils.isMapWorldClass(entity.level())) {
            if (entity instanceof Mob mob) {
                if (ServerContainer.get().DO_NOT_DESPAWN_MAP_MOBS.get()) {
                    // todo have a better check that it was a spawned map mob
                    if (entity instanceof SummonEntity == false) {
                        int count = EntityFinder.start(entity, LivingEntity.class, entity.position()).radius(100).searchFor(AllyOrEnemy.all).build().size();
                        if (ServerContainer.get().DONT_MAKE_MAP_MOBS_PERSISTENT_IF_MOB_COUNT_IS_ABOVE.get() > count) {
                            mob.setPersistenceRequired();
                        }
                    }
                }
            }
        }

        setupNewMobOnSpawn((LivingEntity) entity);

    }

    public static void setupNewMobOnSpawn(LivingEntity entity) {

        if (entity.level().isClientSide) {
            return;
        }

        EntityData endata = Load.Unit(entity);

        if (endata != null) {

            endata.setType();

            Player nearestPlayer = null;

            nearestPlayer = PlayerUtils.nearestPlayer((ServerLevel) entity.level(), entity);

            if (endata.needsToBeGivenStats()) {
                setupNewMob(entity, endata, nearestPlayer);
                entity.heal(Integer.MAX_VALUE);
            } else {
                if (endata.getUnit() == null) {
                    endata.setUnit(new Unit());
                }
                endata.getUnit().initStats(); // give new stats to mob on spawn
                endata.setEquipsChanged();
            }

        }

    }

    public static Unit setupNewMob(LivingEntity entity, EntityData endata, Player nearestPlayer) {
        EntityConfig config = ExileDB.getEntityConfig(entity, endata);

        Unit mob = new Unit();
        mob.initStats();

        endata.SetMobLevelAtSpawn(nearestPlayer);

        String rar = endata.getRarity();

        rar = mob.randomRarity(endata.getLevel(), endata);

        if (config.hasSpecificRarity()) {
            rar = config.set_rar;
        }

        endata.setRarity(rar);

        MobRarity rarity = ExileDB.MobRarities().get(rar);
        endata.getAffixData().randomizeAffixes(rarity);

        endata.setUnit(mob);

        endata.mobStatsAreSet();
        endata.setEquipsChanged();

        return mob;

    }

}
