package fr.rushcubeland.rcbproxy.bukkit.data.redis;

import fr.rushcubeland.rcbproxy.bukkit.RcbProxy;
import org.bukkit.Bukkit;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

public class RedisAccess {

    public static RedisAccess INSTANCE;

    private RedissonClient redissonClient;

    public RedisAccess(RedisCredentials redisCredentials) {
        INSTANCE = this;
        this.redissonClient = initRedisson(redisCredentials);
        Bukkit.getLogger().info("Connection established with Redis Server");
    }

    public static void init(){
        new RedisAccess(new RedisCredentials(RcbProxy.getInstance().getConfig().getString("Redis.ip"), RcbProxy.getInstance().getConfig().getString("Redis.pass"), RcbProxy.getInstance().getConfig().getInt("Redis.port")));
    }

    public static void close(){
        RedisAccess.INSTANCE.getRedissonClient().shutdown();

    }

    public RedissonClient initRedisson(RedisCredentials redisCredentials){
        final Config config = new Config();

        config.setCodec(new JsonJacksonCodec());
        //config.setUseLinuxNativeEpoll(true);
        config.setThreads(8);
        config.setNettyThreads(8);
        config.useSingleServer()
                .setAddress(redisCredentials.toRedisURL())
                .setPassword(redisCredentials.getPassword())
                .setDatabase(RcbProxy.getInstance().getConfig().getInt("Redis.database"))
                .setClientName(redisCredentials.getClientName());

        return Redisson.create(config);

    }

    public RedissonClient getRedissonClient() {
        return redissonClient;
    }
}
