package fr.rushcubeland.rcbproxy.bungee.account;


import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

abstract class AbstractData {

    public UUID uuid;

    public String getUUID() {
        return uuid.toString();
    }

    ProxiedPlayer getPlayer() {
        return RcbProxy.getInstance().getProxy().getPlayer(uuid);
    }
}
