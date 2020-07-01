package fr.rushcubeland.rcbproxy.bukkit.sanction;

import fr.rushcubeland.rcbproxy.bukkit.utils.TimeUnit;

public enum SanctionUnit {

    MESSAGES_INUTILE("Message inutile",  "Mute", 10, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    FAUSSE_INFO("Fausse Information", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    FORMATAGE_INCORRECT("Formatage incorrect", "Mute", 20, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    VENTARDISE("Ventardise", "Mute", 25, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    FLOOD_SPAM("Flood/Spam", "Mute", 35, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    MAUVAIS_LANGAGE("Mauvais langage", "Mute", 40, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    PROVOCATION("Provocation", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    INSULTE("Insulte", "Mute", 50, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    INCITATION_INFRACTION("Incitation à l'infraction", "Mute", 1, TimeUnit.HEURE, TimeUnit.HEURE.getToSecond()),
    LIEN_INTERDIT("Lien interdit", "Mute", 70, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    PUBLICITE("Publicité", "Mute", 80, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    DDOS_HACK_LIEN("DDOS/Hack (lien)", "Ban", 7, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    PSEUDO_INCORRECT("Pseudo incorrect", "Ban", 21, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    MENACE_IRL("Menace IRL", "Ban", 1, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),

    TROLL("Troll", "Mute", 30, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),
    ABUS_REPORT("Abus de report", "Mute", 20, TimeUnit.MINUTE, TimeUnit.MINUTE.getToSecond()),

    ANTI_KB("Anti-knockback", "Ban", 7, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    KILL_AURA("KillAura", "Ban", 8,  TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    FAST_PLACE("FastPlace", "Ban", 6, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    MACRO_CLICK("Macro Click", "Ban", 6, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    REACH("Reach", "Ban", 6, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    FLY("Fly/Glide", "Ban", 8, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),
    TRICHE_AUTRE("Triche Autre", "Ban", 6, TimeUnit.MOIS, TimeUnit.MOIS.getToSecond()),

    SKIN_INCORRECT("Skin incorrect", "Ban", 7, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    ALLY("Alliance prohibée", "Ban", 3, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond()),
    ANTI_JEU("Anti-jeu", "BAN", 1, TimeUnit.JOUR, TimeUnit.JOUR.getToSecond());

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
