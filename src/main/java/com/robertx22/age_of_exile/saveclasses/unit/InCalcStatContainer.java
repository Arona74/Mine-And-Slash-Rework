package com.robertx22.age_of_exile.saveclasses.unit;

import com.robertx22.age_of_exile.database.data.stats.Stat;
import com.robertx22.age_of_exile.database.data.stats.types.UnknownStat;
import com.robertx22.age_of_exile.database.registry.ExileDB;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InCalcStatContainer {


    public transient HashMap<String, InCalcStatData> statsInCalc = new HashMap<>();

    public StatContainer calculate() {
        StatContainer c = new StatContainer();

        statsInCalc.values()
                .forEach(x -> {
                    c.stats.put(x.id, x.getCalculated());
                });
        statsInCalc.clear();
        return c;
    }

    public void modifyInCalc(Consumer<InCalcStatData> co) {
        for (Map.Entry<String, InCalcStatData> en : new HashMap<>(statsInCalc).entrySet()) {
            co.accept(en.getValue());
        }
    }


    public InCalcStatData getStatInCalculation(Stat stat) {
        return getStatInCalculation(stat.GUID());
    }

    public InCalcStatData getStatInCalculation(String guid) {

        InCalcStatData data = statsInCalc.get(guid);

        if (data == null) {
            Stat stat = ExileDB.Stats().get(guid);
            if (stat != null) {
                statsInCalc.put(stat.GUID(), new InCalcStatData(stat.GUID()));
                return statsInCalc.get(stat.GUID());
            } else {
                return new InCalcStatData(new UnknownStat().GUID());
            }
        } else {
            return data;
        }
    }
}
