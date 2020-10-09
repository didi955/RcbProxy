package fr.rushcubeland.rcbproxy.bungee.data.sql;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;

public enum DatabaseManager {

    Main_BDD(new DatabaseCredentials(RcbProxy.getInstance().getConfig().getString("Databases.database1.host"), RcbProxy.getInstance().getConfig().getString("Databases.database1.user"), RcbProxy.getInstance().getConfig().getString("Databases.database1.pass"), RcbProxy.getInstance().getConfig().getString("Databases.database1.dbname"), RcbProxy.getInstance().getConfig().getInt("Databases.database1.port")));

    private DatabaseAccess databaseAccess;

    DatabaseManager(DatabaseCredentials credentials){
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public static void initAllDatabaseConnections() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.databaseAccess.initPool();
        }

    }

    public static void closeAllDatabaseConnection() {
        for(DatabaseManager databaseManager : values()) {
            databaseManager.databaseAccess.closePool();
        }

    }
}
