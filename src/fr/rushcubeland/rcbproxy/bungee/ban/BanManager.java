package fr.rushcubeland.rcbproxy.bungee.ban;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.database.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.database.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BanManager {

    private HashMap<String, String[]> databan = new HashMap<>();
    private final ArrayList<String> dataunban = new ArrayList<>();
    private ArrayList<String> uuidbansbdd = new ArrayList<>();
    private boolean alreadysaved;


    public void ban(UUID uuid, long durationSeconds, String reason){
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis()+(durationSeconds*1000);
        if (durationSeconds == -1L) {
            end = -1L;
        }
        String[] data = {String.valueOf(start), String.valueOf(end), reason};
        this.databan.put(uuid.toString(), data);

        if(ProxyServer.getInstance().getPlayer(uuid) != null) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(uuid);
            ProxyServer.getInstance().broadcast(new TextComponent("§6Le joueur §e" + target.getName() + " §6a été §cbanni §6pour §e" + reason));
            target.disconnect(new TextComponent("§6Vous avez été §cbanni !\n \n §eRaison : §f" + reason + "\n \n §aTemps restant : §f" +
                    getTimeLeft(uuid)));
        }
        if(this.dataunban.contains(uuid.toString())){
            this.dataunban.remove(uuid.toString());
        }

    }

    public boolean isBanned(UUID uuid){
        if(this.databan.containsKey(uuid.toString())){
            return true;
        }
        return false;
    }

    public long getEnd(UUID uuid){
        if(isBanned(uuid)){
            String[] data = this.databan.get(uuid.toString());
            return Long.parseLong(data[1]);
        }
        return 0L;
    }

    public void unban(UUID uuid){
        if(isBanned(uuid)){
            this.databan.remove(uuid.toString());
            this.dataunban.add(uuid.toString());
        }
    }

    public void checkDuration(UUID uuid) {
        if(!isBanned(uuid))
            return;
        if(getEnd(uuid) == -1L)
            return;
        if(getEnd(uuid) < System.currentTimeMillis()) {
            unban(uuid);
        }
    }

    public String getReason(UUID uuid){
        if(isBanned(uuid)){
            String[] data = this.databan.get(uuid.toString());
            return data[2];
        }
        return null;
    }

    public String getTimeLeft(UUID uuid) {
        if (!isBanned(uuid)) return "§cNon banni";
        if (getEnd(uuid) == -1L) {
            return "§cPermanent";
        }
        long tempsRestant = (getEnd(uuid) - System.currentTimeMillis()) / 1000L;
        int mois = 0;
        int jours = 0;
        int heures = 0;
        int minutes = 0;

        while (tempsRestant >= TimeUnit.MOIS.getToSecond()) {
            mois++;
            tempsRestant -= TimeUnit.MOIS.getToSecond();
        }
        while (tempsRestant >= TimeUnit.JOUR.getToSecond()) {
            jours++;
            tempsRestant -= TimeUnit.JOUR.getToSecond();
        }
        while (tempsRestant >= TimeUnit.HEURE.getToSecond()) {
            heures++;
            tempsRestant -= TimeUnit.HEURE.getToSecond();
        }
        while (tempsRestant >= TimeUnit.MINUTE.getToSecond()) {
            minutes++;
            tempsRestant -= TimeUnit.MINUTE.getToSecond();
        }
        return mois + " " + TimeUnit.MOIS.getName() + ", " + jours + " " + TimeUnit.JOUR.getName() + ", " + heures + " " + TimeUnit.HEURE.getName() + ", " + minutes + " " + TimeUnit.MINUTE.getName();
    }

    private void getUUIDOfBanFromMySQL(){

        ArrayList<String> uuidBans = new ArrayList<>();

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), "SELECT uuid from Proxyban", rs -> {

                try {
                    if(rs.next()){
                        uuidBans.add(rs.getString("uuid"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        this.uuidbansbdd = uuidBans;
    }

    private HashMap<String, String[]> getDataOfBanFromMySQL(){

        HashMap<String, String[]> Mapdata = new HashMap<>();

        for(String uuid : uuidbansbdd){
            try {
                MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Proxyban WHERE uuid='%s'", uuid), rs -> {

                    try {
                        if(rs.next()){

                            long start = Long.parseLong(rs.getString("start"));
                            long end = Long.parseLong(rs.getString("end"));
                            String reason = rs.getString("reason");
                            String[] data = {String.valueOf(start), String.valueOf(end), String.valueOf(reason)};
                            Mapdata.put(uuid, data);
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return Mapdata;
    }

    private void sendDataofBanToMySQL(){
        for(String uuid : this.dataunban){
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM Proxyban WHERE uuid='%s'",
                        uuid));

            } catch (SQLException exception) {
                exception.printStackTrace();
            }

        }
        for(Map.Entry entry : this.databan.entrySet()){

            String uuid = (String) entry.getKey();
            String[] data = (String[]) entry.getValue();
            long start = Long.parseLong(data[0]);
            long end = Long.parseLong(data[1]);
            String reason = data[2];

            try {
                MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Proxyban WHERE uuid='%s'", uuid
                ), rs -> {
                    try {
                        if(rs.next()){
                            try {
                                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE Proxyban SET start='%s', end='%s', reason='%s' WHERE uuid='%s'",
                                        start, end, reason, uuid));

                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }
                        }
                        else
                        {
                            try {
                                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Proxyban (uuid, start, end, reason) VALUES ('%s', '%s', '%s', '%s')",
                                        uuid, start, end, reason));
                            } catch (SQLException exception) {
                                exception.printStackTrace();
                            }
                        }
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                });
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void getTaskNoAsync(){
       getUUIDOfBanFromMySQL();
       this.databan = getDataOfBanFromMySQL();
    }

    private void sendTaskNoAsync(){
        sendDataofBanToMySQL();
    }

    public void onEnableProxy(){
        getTaskNoAsync();
    }

    public void onDisableProxy(){
        sendTaskNoAsync();
    }

}
