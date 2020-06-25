package fr.rushcubeland.rcbproxy.bungee;

import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.account.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.commands.Btp;
import fr.rushcubeland.rcbproxy.bungee.database.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.database.MySQL;
import fr.rushcubeland.rcbproxy.bungee.listeners.ProxiedPlayerJoin;
import fr.rushcubeland.rcbproxy.bungee.listeners.ProxiedPlayerQuit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RcbProxy extends Plugin {

    private static RcbProxy instance;
    private List<Account> accounts;
    public static String channel = "rcbproxy:main";

    @Override
    public void onEnable() {
        instance = this;

        ProxyServer.getInstance().registerChannel(channel);
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeReceive());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AutoCompletion());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerJoin());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerQuit());

        accounts = new ArrayList<>();


        initCommands();

        DatabaseManager.initAllDatabaseConnections();
        MySQL.createTables();
        initAllRankPermissions();

    }

    @Override
    public void onDisable() {
        closeAllRankPermissions();
        DatabaseManager.closeAllDatabaseConnection();
        instance = null;
    }

    public static RcbProxy getInstance() {
        return instance;
    }

    private void initCommands(){
        for (String cmd : Btp.getCmds()) {
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new Btp(cmd));
        }
    }

    private void initAllRankPermissions(){
        for(RankUnit rank : RankUnit.values()){
            rank.onEnableProxy();
        }
    }

    private void closeAllRankPermissions(){
        for(RankUnit rank : RankUnit.values()){
            rank.onDisableProxy();
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Optional<Account> getAccount(ProxiedPlayer player){
        return new ArrayList<>(accounts).stream().filter(a -> a.getUUID().equals(player.getUniqueId().toString())).findFirst();
    }

}
