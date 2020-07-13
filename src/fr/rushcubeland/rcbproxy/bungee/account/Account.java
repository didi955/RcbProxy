package fr.rushcubeland.rcbproxy.bungee.account;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.database.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.database.MySQL;
import fr.rushcubeland.rcbproxy.bungee.options.OptionUnit;
import net.md_5.bungee.api.ProxyServer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Account extends AbstractData {

    private boolean newPlayer;
    private final DRank dataRank;
    private final DPermissions dataPermissions;
    private final DFriends dataFriends;
    private final DParty dataParty;
    private final DOptions dataOptions;

    public Account(UUID uuid){
        this.uuid = uuid;
        this.dataRank = new DRank(uuid);
        this.dataPermissions = new DPermissions(uuid);
        this.dataFriends = new DFriends(uuid);
        this.dataParty = new DParty(uuid);
        this.dataOptions = new DOptions(uuid);
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

    private String[] getDataOfProxiedPlayerOptionsFromMySQL() {
        String[] data = new String[4];

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Proxyplayer_options WHERE uuid='%s'", getUUID()), rs -> {
                try {
                    if(rs.next()) {
                        data[0] = rs.getString("state_party_invite");
                        data[1] = rs.getString("state_friend_requests");
                        data[2] = rs.getString("state_chat");
                        data[3] = rs.getString("state_friends_statut_notif");
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


    private ArrayList<String> getDataOfProxiedPlayerFriendsFromMySQL(){
        ArrayList<String> dataFriends = new ArrayList<>();

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT friend FROM Proxyplayer_friends WHERE uuid='%s'",
                    getUUID()), rs -> {

                try {
                    while(rs.next()){

                        dataFriends.add(rs.getString("friend"));

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dataFriends;
    }

    private void sendDataOfProxiedPlayerFriendsToMySQL(){
        for(String friends : dataFriends.getFriends()){

            try {
                MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT friend FROM Proxyplayer_friends WHERE uuid='%s' AND friend='%s'",
                        getUUID(), friends), rs -> {
                    try {
                        if(!rs.next()){
                            try {
                                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Proxyplayer_friends (uuid, friend) VALUES ('%s', '%s')",
                                        getUUID(), friends));

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

    private void sendDataOfProxiedPlayerOptionsToMysql() {
        if(newPlayer) {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Proxyplayer_options (uuid, state_party_invite, state_friend_requests, state_chat, state_friends_statut_notif) VALUES ('%s', '%s', '%s', '%s', '%s')",
                        getUUID(), dataOptions.getStatePartyInvite().getName(), dataOptions.getStateFriendRequests().getName(), dataOptions.getStateChat().getName(), dataOptions.getStateFriendsStatutNotif().getName()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else
        {
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("UPDATE Proxyplayer_options SET state_party_invite='%s', state_friend_requests='%s', state_chat='%s', state_friends_statut_notif='%s' WHERE uuid='%s'",
                        dataOptions.getStatePartyInvite().getName(), dataOptions.getStateFriendRequests().getName(), dataOptions.getStateChat().getName(), dataOptions.getStateFriendsStatutNotif().getName(), getUUID()));
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
        String[] dataPlayerOptions = getDataOfProxiedPlayerOptionsFromMySQL();
        ArrayList<String> dataPlayerperms = getDataOfProxiedPlayerPermissionsFromMySQL();
        ArrayList<String> dataPlayerFriends = getDataOfProxiedPlayerFriendsFromMySQL();

        if(newPlayer){
            dataRank.setRank(RankUnit.JOUEUR);
            dataFriends.setMaxFriends(20);
            dataOptions.setStateChat(OptionUnit.OPEN);
            dataOptions.setStateFriendRequests(OptionUnit.OPEN);
            dataOptions.setStatePartyInvite(OptionUnit.OPEN);
            dataOptions.setStateFriendsStatutNotif(OptionUnit.OPEN);
        }
        else {
            dataRank.setRank(RankUnit.getByName(data[0]), Long.parseLong(data[1]));
            for(String perm : dataPlayerperms){
                dataPermissions.addPermission(perm);
                getPlayer().setPermission(perm, true);
            }
            dataOptions.setStatePartyInvite(OptionUnit.getByName(dataPlayerOptions[0]));
            dataOptions.setStateFriendRequests(OptionUnit.getByName(dataPlayerOptions[1]));
            dataOptions.setStateChat(OptionUnit.getByName(dataPlayerOptions[2]));
            dataOptions.setStateFriendsStatutNotif(OptionUnit.getByName(dataPlayerOptions[3]));
            if(dataRank.getRank().equals(RankUnit.VIP)){
                dataFriends.setMaxFriends(30);
            }
            else if(dataRank.getRank().equals(RankUnit.VIPP)){
                dataFriends.setMaxFriends(40);
            }
            else {
                dataFriends.setMaxFriends(50);
            }
            ProxyServer.getInstance().getScheduler().schedule(RcbProxy.getInstance(), () -> {
                if(getPlayer().getServer() != null){
                    for(String friends : dataPlayerFriends){
                        dataFriends.addFriend(friends);
                        BungeeSend.sendFriendsDataAdd(getPlayer(), friends);
                    }
                }
            }, 5L, TimeUnit.SECONDS);
        }
    }

    private void sendTasks(){
        sendDataOfProxiedPlayerToMysql();
        sendDataOfProxiedPlayerOptionsToMysql();
        sendDataOfProxiedPlayerPermissionsToMySQL();
        sendDataOfProxiedPlayerFriendsToMySQL();
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

    public DFriends getDataFriends() {
        return dataFriends;
    }

    public DParty getDataParty() {
        return dataParty;
    }

    public DOptions getDataOptions() {
        return dataOptions;
    }
}
