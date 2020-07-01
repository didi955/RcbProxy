package fr.rushcubeland.rcbproxy.bungee.account;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.database.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.database.MySQL;
import net.md_5.bungee.api.ProxyServer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class Account extends AbstractData {

    private boolean newPlayer;
    private final DRank dataRank;
    private final DPermissions dataPermissions;

    public Account(UUID uuid){
        this.uuid = uuid;
        this.dataRank = new DRank(uuid);
        this.dataPermissions = new DPermissions(uuid);
    }

    private String[] getDataOfProxiedPlayerFromMySQL() {
        String[] data = new String[2];

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Proxyaccounts WHERE uuid='%s'", getUUID()), rs -> {
                try {
                    if(rs.next()) {
                        data[0] = rs.getString("grade");
                        data[1] = rs.getString("grade_end");
                    }
                    else
                    {
                        newPlayer = true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return data;
    }

    private ArrayList<String> getDataOfProxiedPlayerPermissionsFromMySQL(){
        ArrayList<String> dataPlayerperms = new ArrayList<>();

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM Proxyplayer_permissions WHERE uuid='%s'",
                    getUUID()), rs -> {

                try {
                    while(rs.next()){

                        dataPlayerperms.add(rs.getString("permission"));

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dataPlayerperms;
    }

    private void sendDataOfProxiedPlayerToMysql() {
        if(newPlayer) {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Proxyaccounts (uuid, grade, grade_end) VALUES ('%s', '%s', '%s')",
                        getUUID(), dataRank.getRank().getName(), dataRank.getEnd()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE Proxyaccounts SET grade='%s', grade_end='%s' WHERE uuid='%s'",
                        dataRank.getRank().getName(), dataRank.getEnd(), getUUID()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void sendDataOfProxiedPlayerPermissionsToMySQL(){
        for(String perms : dataPermissions.getPermissions()){

            try {
                MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT permission FROM Proxyplayer_permissions WHERE uuid='%s' AND permission='%s'",
                    getUUID(), perms), rs -> {
                    try {
                        if(!rs.next()){
                            try {
                                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Proxyplayer_permissions (uuid, permission) VALUES ('%s', '%s')",
                                        getUUID(), perms));

                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
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

    private void getTasks(){
        String[] data = getDataOfProxiedPlayerFromMySQL();
        ArrayList<String> dataPlayerperms = getDataOfProxiedPlayerPermissionsFromMySQL();

        if(newPlayer){
            dataRank.setRank(RankUnit.JOUEUR);
        }
        else {
            dataRank.setRank(RankUnit.getByName(data[0]), Long.parseLong(data[1]));
            for(String perm : dataPlayerperms){
                dataPermissions.addPermission(perm);
                getPlayer().setPermission(perm, true);
            }
        }
    }

    private void sendTasks(){
        sendDataOfProxiedPlayerToMysql();
        sendDataOfProxiedPlayerPermissionsToMySQL();
    }


    public void onLogin() {
        RcbProxy.getInstance().getAccounts().add(this);
        getTasks();
    }

    public void onLogout() {
        sendTasks();
        RcbProxy.getInstance().getAccounts().remove(this);
    }

    public DRank getDatarank() {
        return dataRank;
    }

    public DPermissions getDataPermissions() {
        return dataPermissions;
    }

}
