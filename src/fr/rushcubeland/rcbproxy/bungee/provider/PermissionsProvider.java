package fr.rushcubeland.rcbproxy.bungee.provider;

import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.rcbproxy.bungee.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bungee.data.sql.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.data.sql.MySQL;
import fr.rushcubeland.rcbproxy.bungee.exceptions.AccountNotFoundException;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class PermissionsProvider {

    public static final String REDIS_KEY = "permissions:";
    public static final APermissions DEFAULT_ACCOUNT = new APermissions(UUID.randomUUID(), new ArrayList<>());

    private ProxiedPlayer player;
    private RedisAccess redisAccess;
    private UUID uuid;

    public PermissionsProvider(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.redisAccess = RedisAccess.INSTANCE;
    }

    public APermissions getAccount() throws AccountNotFoundException {

        APermissions account = getPermissionsFromRedis();

        if(account == null){
            account = getPermissionsFromDatabase();

            sendPermissionsToRedis(account);

        }
        return account;
    }

    public void sendPermissionsToRedis(APermissions account){
        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = REDIS_KEY + uuid.toString();
        final RBucket<APermissions> accountRBucket = redissonClient.getBucket(key);

        accountRBucket.set(account);
    }

    private APermissions getPermissionsFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        final String key = REDIS_KEY + this.player.getUniqueId().toString();
        final RBucket<APermissions> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private APermissions getPermissionsFromDatabase() throws AccountNotFoundException {

        ArrayList<String> perms = new ArrayList<>();

        try {
            MySQL.query(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("SELECT * FROM Permissions WHERE uuid='%s'", this.player.getUniqueId().toString()), rs -> {
                try {

                    while(rs.next()) {

                        String permission = rs.getString("permission");
                        perms.add(permission);

                    }

                    return new APermissions(uuid, perms);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


}
