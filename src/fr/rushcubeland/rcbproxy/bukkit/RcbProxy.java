package fr.rushcubeland.rcbproxy.bukkit;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bukkit.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bukkit.listeners.*;
import fr.rushcubeland.rcbproxy.bukkit.mod.ModModeratorTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RcbProxy extends JavaPlugin {

    private static RcbProxy instance;
    private final String channel = "rcbproxy:main";

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channel);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, channel, new BukkitReceive());

        registerEvents();

        RedisAccess.init();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ModModeratorTask(), 1, 20L);

    }

    @Override
    public void onDisable() {
        instance = null;
        RedisAccess.close();
    }
    
    private void registerEvents(){
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new BukkitReceive(), this);
        pm.registerEvents(new ClickEvent(), this);
        pm.registerEvents(new ChatEvent(), this);
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new FoodChange(), this);
        pm.registerEvents(new PlaceBlock(), this);
        pm.registerEvents(new BreackBlock(), this);
        pm.registerEvents(new DamageEvent(), this);
        pm.registerEvents(new PickupEvent(), this);
        pm.registerEvents(new DropEvent(), this);
        pm.registerEvents(new QuitEvent(), this);
    }

    public AOptions getAccountOptions(Player player){

        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = "options:" + player.getUniqueId();
        final RBucket<AOptions> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    public AFriends getAccountFriends(Player player){

        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = "friends:" + player.getUniqueId();
        final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    public APermissions getAccountPermissions(Player player){

        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = "permissions:" + player.getUniqueId();
        final RBucket<APermissions> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    public Account getAccount(Player player){

        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = "account:" + player.getUniqueId();
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    public Account getAccount(UUID uuid){

        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = "account:" + uuid;
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    public void sendAccountToRedis(Account account){
        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = "account:" + account.getUuid();
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        accountRBucket.set(account);
    }


    public static RcbProxy getInstance() {
        return instance;
    }
}
