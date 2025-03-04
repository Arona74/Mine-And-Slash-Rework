package com.robertx22.age_of_exile.loot.req;

import com.robertx22.age_of_exile.database.data.league.LeagueMechanic;

public class DropRequirement {

    private String league = "";
    int req_lvl = 0;

    private DropRequirement() {

    }

    public boolean canDropInLeague(LeagueMechanic m, int lvl) {
        if (lvl < req_lvl) {
            return false;
        }
        if (league.isEmpty()) {
            return true;
        } else {
            return m.GUID().equals(league);
        }
    }

    public boolean isFromLeague(LeagueMechanic m) {
        return m.GUID().equals(league);
    }


    public static class Builder {
        DropRequirement r;

        public Builder(DropRequirement r) {
            this.r = r;
        }

        public static Builder of() {
            return new Builder(new DropRequirement());
        }

        public Builder setOnlyDropsInLeague(String le) {
            r.league = le;
            return this;
        }

        public Builder setLevelReq(int lvl) {
            r.req_lvl = lvl;
            return this;
        }

        public DropRequirement build() {
            return r;
        }

    }

}
