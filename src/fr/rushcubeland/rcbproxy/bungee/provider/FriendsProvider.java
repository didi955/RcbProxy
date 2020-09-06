package fr.rushcubeland.rcbproxy.bungee.provider;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bungee.data.sql.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.data.sql.MySQL;
import fr.rushcubeland.rcbproxy.bungee.exceptions.AccountNotFoundException;
import fr.rushcubeland.rcbproxy.bungee.rank.RankUnit;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendsProvider {

    public static final String REDIS_KEY = "friends:";
    public static final AFriends DEFAULT_ACCOUNT = new AFriends(UUID.randomUUID(), 20, new ArrayList<>());

    private ProxiedPlayer player;
    private RedisAccess redisAccess;
    private UUID uuid;

    public FriendsProvider(ProxiedPlayer player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.redisAccess = RedisAccess.INSTANCE;
    }

    public AFriends getAccount() throws AccountNotFoundException {

        AFriends account = getFriendsFromRedis();

        if(account == null){
            account = getFriendsFromDatabase();
            sendFriendsToRedis(account);
        }
        return account;
    }

    public void sendFriendsToRedis(AFriends account){
        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = REDIS_KEY + uuid.toString();
        final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

        accountRBucket.set(account);
    }

    private AFriends getFriendsFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        final String key = REDIS_KEY + this.player.getUniqueId().toString();
        final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private AFriends getFriendsFromDatabase() {

        AFriends aFriends = null;
        List<UUID> friends = new ArrayList<>();

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Friends WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            while(rs.next()){

                final UUID uuid = UUID.fromString(rs.getString("friend"));
                friends.add(uuid);

            }

            aFriends = new AFriends(uuid, 20, friends);

            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return aFriends;
    }

}
