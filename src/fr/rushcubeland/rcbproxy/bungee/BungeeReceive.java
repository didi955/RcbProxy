package fr.rushcubeland.rcbproxy.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.account.RankUnit;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;

public class BungeeReceive implements Listener {

    @EventHandler
    public void on(PluginMessageEvent event)
    {
        if ( !event.getTag().equalsIgnoreCase( "rcbproxy:main" ) )
        {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput( event.getData());
        String subChannel = in.readUTF();

        if(subChannel.equalsIgnoreCase( "ChangeRank")){
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(in.readUTF());
            RankUnit rank = RankUnit.getByName(in.readUTF());
            long seconds = in.readLong();

            Optional<Account> account = RcbProxy.getInstance().getAccount(player);
            if(account.isPresent()){
                account.get().getDatarank().setRank(rank, seconds);
            }
        }
    }
}

