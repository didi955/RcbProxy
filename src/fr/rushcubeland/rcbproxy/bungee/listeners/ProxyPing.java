package fr.rushcubeland.rcbproxy.bungee.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ProxyPing implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onProxyPing(ProxyPingEvent e) {
        //e.getResponse().getPlayers().setMax(ProxyServer.getInstance().getConfig().getPlayerLimit());
    }
}
