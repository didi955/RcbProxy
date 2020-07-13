package fr.rushcubeland.rcbproxy.bungee;

import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.account.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.ban.BanManager;
import fr.rushcubeland.rcbproxy.bungee.commands.*;
import fr.rushcubeland.rcbproxy.bungee.listeners.ProxyPing;
import fr.rushcubeland.rcbproxy.bungee.mute.CheckMuteStateTask;
import fr.rushcubeland.rcbproxy.bungee.mute.MuteManager;
import fr.rushcubeland.rcbproxy.bungee.parties.Party;
import fr.rushcubeland.rcbproxy.bungee.utils.TimeUnit;
import fr.rushcubeland.rcbproxy.bungee.database.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.database.MySQL;
import fr.rushcubeland.rcbproxy.bungee.listeners.ProxiedPlayerJoin;
import fr.rushcubeland.rcbproxy.bungee.listeners.ProxiedPlayerQuit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class RcbProxy extends Plugin {

    private static RcbProxy instance;
    private List<Account> accounts;
    private List<Party> parties;
    public static String channel = "rcbproxy:main";

    private BanManager banManager;
    private MuteManager muteManager;

    private final HashMap<ProxiedPlayer, ProxiedPlayer> mpData = new HashMap<>();

    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;

        ProxyServer.getInstance().registerChannel(channel);
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeReceive());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new AutoCompletion());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerJoin());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxiedPlayerQuit());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new ProxyPing());

        accounts = new ArrayList<>();
        parties = new ArrayList<>();

        initCommands();
        TimeUnit.initTimeUnit();

        DatabaseManager.initAllDatabaseConnections();
        MySQL.createTables();
        initAllRankPermissions();

        this.banManager = new BanManager();
        banManager.onEnableProxy();
        this.muteManager = new MuteManager();
        muteManager.onEnableProxy();

        ProxyServer.getInstance().getScheduler().schedule(this, new CheckMuteStateTask(), 1, 3, java.util.concurrent.TimeUnit.SECONDS);

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
        for(String cmd : FriendCommand.getCmds()) {
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new FriendCommand(cmd));
        }
        for(String cmd : HubCommand.getCmds()){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new HubCommand(cmd));
        }
        for(String cmd : ReplyCommand.getCmds()){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new ReplyCommand(cmd));
        }
        for(String cmd : PartyCommand.getCmds()){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new PartyCommand(cmd));
        }
        /*
        for(String cmd : SlotsCommand.getCmds()){
            ProxyServer.getInstance().getPluginManager()
                    .registerCommand(this, new SlotsCommand(cmd));
        }

         */
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnbanCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WhoisCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnmuteCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishGUICommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new KickCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ModModeratorCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new StaffChatCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReportCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new MPCommand());
    }

    private void loadConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration", e);
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

    public List<Party> getParties() {
        return parties;
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

    public HashMap<ProxiedPlayer, ProxiedPlayer> getMpData() {
        return mpData;
    }

    public Configuration getConfig() {
        return config;
    }
}
