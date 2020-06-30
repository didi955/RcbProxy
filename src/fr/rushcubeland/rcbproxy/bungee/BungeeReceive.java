package fr.rushcubeland.rcbproxy.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class BungeeReceive implements Listener {


    @EventHandler
    public void on(PluginMessageEvent event){
        if (!event.getTag().equalsIgnoreCase( RcbProxy.getInstance().getChannel())){
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();

        if(subChannel.equalsIgnoreCase( "Ban")){
            String uuid = in.readUTF();
            long durationSeconds = in.readLong();
            String reason = in.readUTF();
            RcbProxy.getInstance().getBanManager().ban(UUID.fromString(uuid), durationSeconds, reason);

        }
        if(subChannel.equalsIgnoreCase("Mute")){
            String uuid = in.readUTF();
            long durationSeconds = in.readLong();
            String reason = in.readUTF();
            RcbProxy.getInstance().getMuteManager().mute(UUID.fromString(uuid), durationSeconds, reason);

        }
        if(subChannel.equalsIgnoreCase("Kick")){
            String targetname = in.readUTF();
            String reason = in.readUTF();
            ProxyServer.getInstance().getPlayer(targetname).disconnect(new TextComponent("§cVous avez été kick ! \n \n §6Raison: §e" + reason));
        }

    }


}

