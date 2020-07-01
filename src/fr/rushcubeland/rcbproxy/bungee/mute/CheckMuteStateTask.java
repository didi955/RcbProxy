package fr.rushcubeland.rcbproxy.bungee.mute;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CheckMuteStateTask implements Runnable {

    @Override
    public void run() {
        for(ProxiedPlayer pls : ProxyServer.getInstance().getPlayers()){
            if(RcbProxy.getInstance().getMuteManager().isMuted(pls.getUniqueId())){
                RcbProxy.getInstance().getMuteManager().checkDuration(pls.getUniqueId());
            }
        }
    }
}