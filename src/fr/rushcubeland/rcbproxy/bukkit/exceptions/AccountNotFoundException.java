package fr.rushcubeland.rcbproxy.bukkit.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(UUID uuid){
        super("The account (" + uuid.toString() + ") cannot be found");
    }
}
