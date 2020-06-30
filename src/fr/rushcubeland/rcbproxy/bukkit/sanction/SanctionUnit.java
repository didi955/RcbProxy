package fr.rushcubeland.rcbproxy.bukkit.sanction;

import fr.rushcubeland.rcbproxy.bukkit.utils.TimeUnit;

public enum SanctionUnit {

    MESSAGES_INUTILE("Message inutile",  "Mute", 10, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),

    TROLL("Troll", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),

    ANTI_KB("Anti-knockback", "Ban", 5, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    KILL_AURA("KillAura", "Ban", 6,  TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    FAST_PLACE("FastPlace", "Ban", 3, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    MACRO_CLICK("Macro Click", "Ban", 3, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    REACH("Reach", "Ban", 5, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    FLY("Fly/Glide", "Ban", 6, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    TRICHE_AUTRE("Triche Autre", "Ban", 2, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),

    SKIN_INCORRECT("Skin incorrect", "Ban", 7, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    ALLY("Alliance prohibée", "Ban", 7, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond());

    private String motif;
    private String sanctionCmd;
    private long durationD;
    private int multiplier;
    private TimeUnit timeUnit;

    private long durationSeconds;
    

    SanctionUnit(String motif, String sanctionCmd, int multiplier, TimeUnit timeUnit, long durationD){
        this.motif = motif;
        this.sanctionCmd = sanctionCmd;
        this.multiplier = multiplier;
        this.timeUnit = timeUnit;
        this.durationD = durationD;
        this.durationSeconds = multiplier*durationD;
    }

    public String getMotif() {
        return motif;
    }

    public String getSanctionCmd() {
        return sanctionCmd;
    }

    public long getDurationD() {
        return durationD;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
