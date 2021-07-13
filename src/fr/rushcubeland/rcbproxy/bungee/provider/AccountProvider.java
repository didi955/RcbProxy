package fr.rushcubeland.rcbproxy.bungee.provider;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bungee.data.sql.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.data.sql.MySQL;
import fr.rushcubeland.rcbproxy.bungee.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.rcbproxy.bungee.rank.RankUnit;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AccountProvider {

    public static final String REDIS_KEY = "account:";
    public static final Account DEFAULT_ACCOUNT = new Account(UUID.randomUUID(), null, RankUnit.JOUEUR, 0);

    private final ProxiedPlayer player;
    private final RedisAccess redisAccess;
    private final UUID uuid;

    public AccountProvider(ProxiedPlayer player) {
        this.player = player;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = player.getUniqueId();
    }

    public AccountProvider(UUID uuid) {
        this.player = null;
        this.redisAccess = RedisAccess.INSTANCE;
        this.uuid = uuid;
    }

    public Account getAccount() throws AccountNotFoundException {

        Account account = getAccountFromRedis();

        if(account == null){
            account = getAccountFromDatabase();
            sendAccountToRedis(account);
        }
        return account;
    }

    public void sendAccountToRedis(Account account){
        final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
        final String key = REDIS_KEY + uuid.toString();
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        accountRBucket.set(account);
    }

    private Account getAccountFromRedis(){
        final RedissonClient redissonClient = redisAccess.getRedissonClient();
        final String key = REDIS_KEY + this.player.getUniqueId().toString();
        final RBucket<Account> accountRBucket = redissonClient.getBucket(key);

        return accountRBucket.get();
    }

    private Account getAccountFromDatabase() {

        Account account = null;

        try {

            final Connection connection = DatabaseManager.Main_BDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE uuid=?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet rs = preparedStatement.getResultSet();

            if(rs.next()){
                
                RankUnit rank = null;
                if(!rs.getString("primaryRank").equals("NULL")){
                    rank = RankUnit.getByName(rs.getString("primaryRank"));
                }
                final RankUnit rank2 = RankUnit.getByName(rs.getString("secondaryRank"));
                final long rank_end = rs.getLong("primaryRank_end");
                final long rank2_end = rs.getLong("secondaryRank_end");
                final long coins = rs.getLong("coins");

                account = new Account(uuid, rank, rank2, rank_end, rank2_end, coins);

            }
            else
            {
                account = createNewAccount(uuid);
            }

            connection.close();

        } catch (SQLException e){
            e.printStackTrace();
        }

        return account;
    }

    private Account createNewAccount(UUID uuid){

        final Account account = DEFAULT_ACCOUNT.clone();

        try {

            MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("INSERT INTO Accounts (uuid, primaryRank, secondaryRank, primaryRank_end, secondaryRank_end, coins) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                    uuid.toString(), "NULL", account.getSecondaryRank().getName(), account.getPrimaryRank_end(), account.getSecondaryRank_end(), account.getCoins()));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        account.setUuid(uuid);

        return account;
    }
}