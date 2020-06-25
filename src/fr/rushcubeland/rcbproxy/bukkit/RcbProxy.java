package fr.rushcubeland.rcbproxy.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RcbProxy extends JavaPlugin {

    private static RcbProxy instance;
    private String channel = "rcbproxy:main";
    private String channelRank = "rcbproxy:rank";

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channelRank);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, channel, new BukkitReceive());

        Bukkit.getServer().getPluginManager().registerEvents(new BukkitReceive(), this);

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static RcbProxy getInstance() {
        return instance;
    }
}
