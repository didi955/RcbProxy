package fr.rushcubeland.rcbproxy.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.report.Report;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Optional;
import java.util.UUID;

public class BungeeReceive implements Listener {


    @EventHandler
    public void on(PluginMessageEvent event){
        if (!event.getTag().equalsIgnoreCase(RcbProxy.getInstance().getChannel())){
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
        if(subChannel.equalsIgnoreCase("StateFriendsStatutNotif")){
            String targetName = in.readUTF();
            String state = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                Optional<Account> account = RcbProxy.getInstance().getAccount(player);
                account.ifPresent(value -> value.getDataOptions().setStateFriendsStatutNotif(OptionUnit.getByName(state)));
            }
        }
        if(subChannel.equalsIgnoreCase("StateChat")){
            String targetName = in.readUTF();
            String state = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                Optional<Account> account = RcbProxy.getInstance().getAccount(player);
                account.ifPresent(value -> value.getDataOptions().setStateChat(OptionUnit.getByName(state)));
            }
        }
        if(subChannel.equalsIgnoreCase("StateFriendRequests")){
            String targetName = in.readUTF();
            String state = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                Optional<Account> account = RcbProxy.getInstance().getAccount(player);
                account.ifPresent(value -> value.getDataOptions().setStateFriendRequests(OptionUnit.getByName(state)));
            }
        }
        if(subChannel.equalsIgnoreCase("StatePartyInvite")){
            String targetName = in.readUTF();
            String state = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                Optional<Account> account = RcbProxy.getInstance().getAccount(player);
                account.ifPresent(value -> value.getDataOptions().setStatePartyInvite(OptionUnit.getByName(state)));
            }
        }
        if(subChannel.equalsIgnoreCase("StateMP")){
            String targetName = in.readUTF();
            String state = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                Optional<Account> account = RcbProxy.getInstance().getAccount(player);
                account.ifPresent(value -> value.getDataOptions().setStateMP(OptionUnit.getByName(state)));
            }
        }



        if(subChannel.equalsIgnoreCase("CmdProxy")){
            String targetName = in.readUTF();
            String cmd = in.readUTF();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(targetName);
            if(player != null){
                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, cmd);
            }
        }
    }

}

