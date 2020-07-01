package fr.rushcubeland.rcbproxy.bukkit;

import fr.rushcubeland.rcbproxy.bukkit.listeners.*;
import fr.rushcubeland.rcbproxy.bukkit.mod.ModModeratorTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RcbProxy extends JavaPlugin {

    private static RcbProxy instance;
    private String channel = "rcbproxy:main";

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, channel, new BukkitReceive());

        Bukkit.getServer().getPluginManager().registerEvents(new BukkitReceive(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ClickEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FoodChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlaceBlock(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BreackBlock(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PickupEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DropEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitEvent(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ModModeratorTask(), 1, 20L);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static RcbProxy getInstance() {
        return instance;
    }
}
