package fr.rushcubeland.rcbproxy.bukkit;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.rcbproxy.bukkit.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bukkit.listeners.*;
import fr.rushcubeland.rcbproxy.bukkit.mod.ModModeratorTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

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

        RedisAccess.init();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ModModeratorTask(), 1, 20L);

    }

    @Override
    public void onDisable() {
        instance = null;
        RedisAccess.close();
    }

    public interface Callback<T>
    {
        void execute(T response);
    }

    public void getAccountOptionsCallback(Player player, final Callback<AOptions> callback){

        Bukkit.getScheduler().runTaskAsynchronously(RcbProxy.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "options:" + player.getUniqueId().toString();
            final RBucket<AOptions> accountRBucket = redissonClient.getBucket(key);

            final AOptions account = accountRBucket.get();

            Bukkit.getScheduler().runTask(RcbProxy.getInstance(), () -> {

                callback.execute(account);
            });
        });
    }

    public void getAccountFriendsCallback(Player player, final Callback<AFriends> callback){

        Bukkit.getScheduler().runTaskAsynchronously(RcbProxy.getInstance(), () -> {

            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = "friends:" + player.getUniqueId().toString();
            final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

            final AFriends account = accountRBucket.get();

            Bukkit.getScheduler().runTask(RcbProxy.getInstance(), () -> {

                callback.execute(account);
            });
        });
    }

    public static RcbProxy getInstance() {
        return instance;
    }
}
