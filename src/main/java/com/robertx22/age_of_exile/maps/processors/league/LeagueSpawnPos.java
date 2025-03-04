package com.robertx22.age_of_exile.maps.processors.league;

import com.robertx22.age_of_exile.database.data.league.LeagueStructure;
import com.robertx22.age_of_exile.maps.generator.ChunkProcessData;
import com.robertx22.age_of_exile.maps.processors.DataProcessor;
import com.robertx22.age_of_exile.uncommon.datasaving.Load;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;


public class LeagueSpawnPos extends DataProcessor {

    public LeagueSpawnPos() {
        super("league_spawn_pos", Type.EQUALS);
    }

    @Override
    public boolean canSpawnLeagueMechanic() {
        return false;
    }

    @Override
    public void processImplementation(String key, BlockPos pos, Level world, ChunkProcessData data) {

        try {
            var mech = LeagueStructure.getMechanicFromPosition((ServerLevel) world, pos);
            Load.mapAt(world, pos).leagues.get(mech).spawn_pos = pos.asLong();
        } catch (Exception e) {
            //   throw new RuntimeException(e);
        }

    }
}
