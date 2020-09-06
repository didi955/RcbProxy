package fr.rushcubeland.rcbproxy.bungee.provider;

import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.rcbproxy.bungee.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bungee.data.sql.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.data.sql.MySQL;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.exceptions.AccountNotFoundException;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OptionsProvider {

    public static final String REDIS_KEY = "options:";
    public static final AOptions DEFAULT_ACCOUNT = new AOptions(UUID.randomUUID(), OptionUnit.OPEN, OptionUnit.OPEN, OptionUnit.OPEN, OptionUnit.OPEN, OptionUnit.OPEN);

    private ProxiedPlayer player;
    private RedisAccess redisAccess;
    private UUID uuid;

    public OptionsProvider(ProxiedPlayer player) {
        this.player = player;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = player.getUniqueId();
    }

    public AOptions getAccount() throws AccountNotFoundException {

        AOptions account = getAccountFromRedis();

        if(account == null){
            account = getAccountFromDatabase();

            sendAccountToRedis(account);
        }
        return account;
    }

    public void sendAccountToRedis(AOptions account){
        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = REDIS_KEY + uuid.toString();
        final RBucket<AOptions> accountRBucket = redissonClient.getBucket(key);

        accountRBucket.set(account);
    }

    private AOptions getAccountFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        final String key = REDIS_KEY + this.player.getUniqueId().toString();
        final RBucket<AOptions> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private AOptions getAccountFromDatabase() throws AccountNotFoundException {

        AOptions aOptions = null;

        try {
            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Options WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

                    if(rs.next()) {

                        final OptionUnit state1 = OptionUnit.getByName(rs.getString("state_party_invite"));
                        final OptionUnit state2 = OptionUnit.getByName(rs.getString("state_friend_requests"));
                        final OptionUnit state3 = OptionUnit.getByName(rs.getString("state_chat"));
                        final OptionUnit state4 = OptionUnit.getByName(rs.getString("state_friends_statut_notif"));
                        final OptionUnit state5 = OptionUnit.getByName(rs.getString("state_private_msg"));

                        aOptions = new AOptions(uuid, state1, state2, state3, state4, state5);
                    }
                    else
                    {
                        aOptions = createNewAccount(uuid);
                    }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return aOptions;
    }

    private AOptions createNewAccount(UUID uuid){

        final AOptions account = DEFAULT_ACCOUNT.clone();

        try {

            MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Options (uuid, state_party_invite, state_friend_requests, state_chat, state_friends_statut_notif, state_private_msg) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                    uuid.toString(), account.getStatePartyInvite().getName(), account.getStateFriendRequests().getName(), account.getStateChat().getName(), account.getStateFriendsStatutNotif().getName(), account.getStateMP().getName()));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        account.setUuid(uuid);

        return account;
    }

}
