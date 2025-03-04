package com.robertx22.age_of_exile.database.data.stats.datapacks.test;

import com.robertx22.age_of_exile.aoe_data.database.stats.base.EmptyAccessor;
import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.database.registry.ExileDB;
import com.robertx22.library_of_exile.registry.IGUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataPackStatAccessor<T extends IGUID> {

    private HashMap<T, String> map = new HashMap<>();
    private HashMap<T, Stat> map2 = new HashMap<>();

    public boolean has(T key) {
        return map.containsKey(key);
    }

    public Stat get(T key) {
        Stat stat;

        if (!ExileDB.Stats().isRegistered(getId(key))) {
            stat = map2.get(key);
            if (stat == null) {
                throw new RuntimeException(key.GUID() + " is null");
            }
            return stat;
        }

        stat = ExileDB.Stats()
                .get(map.get(key));
        Objects.requireNonNull(stat, "Null for " + key.toString());
        return stat;
    }

    public String getId(T key) {
        return map.get(key);
    }

    public String getId() {
        return getId((T) EmptyAccessor.INSTANCE);
    }

    public void add(T key, DatapackStat stat) {
        map.put(key, stat.id);
        map2.put(key, stat);
    }

    public List<Stat> getAll() {

        if (ExileDB.Stats()
                .isRegistered(map2.values()
                        .stream()
                        .findFirst()
                        .get())) {

            return map.values()
                    .stream()
                    .map(x -> ExileDB.Stats()
                            .get(x))
                    .collect(Collectors.toList());
        }
        // if in dev environment, just use the data gen ones
        return new ArrayList<>(map2.values());
    }

    public Stat get() {
        if (this.map.size() > 1) {
            throw new RuntimeException("Using simple .get() for stat that isn't just a single stat!");
        }
        return get((T) EmptyAccessor.INSTANCE);
    }
}
