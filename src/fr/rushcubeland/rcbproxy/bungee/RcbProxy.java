package fr.rushcubeland.rcbproxy.bungee;

import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.account.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.ban.BanManager;
import fr.rushcubeland.rcbproxy.bungee.commands.*;
import fr.rushcubeland.rcbproxy.bungee.listeners.Chat;
import fr.rushcubeland.rcbproxy.bungee.mute.MuteManager;
import fr.rushcubeland.rcbproxy.bungee.utils.TimeUnit;
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

    private BanManager banManager;
    private MuteManager muteManager;

    @Override
    public void onEnable() {
        instance = this;

        ProxyServer.getInstance().registerChannel(channel);
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeReceive());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AutoCompletion());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerJoin());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerQuit());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new Chat());

        accounts = new ArrayList<>();

        initCommands();
        TimeUnit.initTimeUnit();

        DatabaseManager.initAllDatabaseConnections();
        MySQL.createTables();
        initAllRankPermissions();

        this.banManager = new BanManager();
        banManager.onEnableProxy();
        this.muteManager = new MuteManager();
        muteManager.onEnableProxy();

    }

    @Override
    public void onDisable() {
        closeAllRankPermissions();
        getBanManager().onDisableProxy();
        DatabaseManager.closeAllDatabaseConnection();
        instance = null;
    }

    public static RcbProxy getInstance() {
        return instance;
    }

    private void initCommands(){
        for(String cmd : Btp.getCmds()) {
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new Btp(cmd));
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WhoisCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnmuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishGUICommand());
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

    public BanManager getBanManager() {
        return banManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public String getChannel() {
        return channel;
    }
}
