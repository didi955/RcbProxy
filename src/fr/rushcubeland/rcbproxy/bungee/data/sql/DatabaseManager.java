package fr.rushcubeland.rcbproxy.bungee.data.sql;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;

public enum DatabaseManager {

    Main_BDD(new DatabaseCredentials("192.168.1.2", "****", "*****", "rcb_core", 3306));

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
