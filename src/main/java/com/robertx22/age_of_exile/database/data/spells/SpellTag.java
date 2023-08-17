package com.robertx22.age_of_exile.database.data.spells;

public enum SpellTag {
    projectile("Projectile"),
    movement("Movement"),
    damage("Damage"),
    summon("Summon"),
    heal("Heal"),
    arrow("Arrow"),
    technique("Technique"),
    curse("Curse"),
    shield("Shield"),
    shout("Shout"),
    trap("Trap"),
    beast("Beast"),
    song("Song"),
    chaining("Chaining"),
    golem("Golem"),
    area("Area"),
    staff_spell("Staff Spell"),
    totem("Totem");

    public String locname;

    SpellTag(String locname) {
        this.locname = locname;
    }
}
