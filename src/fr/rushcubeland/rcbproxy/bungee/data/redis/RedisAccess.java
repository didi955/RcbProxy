package fr.rushcubeland.rcbproxy.bungee.data.redis;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.BungeeCord;
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
        BungeeCord.getInstance().getLogger().info("Connection established with Redis Server");
    }

    public static void init(){
        new RedisAccess(new RedisCredentials(String.valueOf(RcbProxy.getInstance().getConfig().get("Redis.ip")), RcbProxy.getInstance().getConfig().getString("Redis.pass"), RcbProxy.getInstance().getConfig().getInt("Redis.port")));
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
