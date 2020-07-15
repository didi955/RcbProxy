package fr.rushcubeland.rcbproxy.bukkit;

import fr.rushcubeland.rcbproxy.bukkit.friends.FriendsGUI;
import fr.rushcubeland.rcbproxy.bukkit.mod.ModModerator;
import fr.rushcubeland.rcbproxy.bukkit.options.Options;
import fr.rushcubeland.rcbproxy.bukkit.sanction.MuteData;
import fr.rushcubeland.rcbproxy.bukkit.sanction.SanctionGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

public class BukkitReceive implements PluginMessageListener, Listener {

    private String channel = "rcbproxy:main";

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(channel))
            return;
        String action = null;

        ArrayList<String> received = new ArrayList<String>();

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            action = in.readUTF();

            while (in.available() > 0) {
                received.add(in.readUTF());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (action == null)
            return;
        if(action.equalsIgnoreCase("Teleport")){
            Player from = Bukkit.getServer().getPlayer((String)received.get(0));
            Player to = Bukkit.getServer().getPlayer((String)received.get(1));
            if(to == null){
                from.sendMessage("§cLe joueur s'est déconnecté");
                return;
            }
            from.teleport(to);
        }
        if(action.equalsIgnoreCase("PunishGUI")){
            Player mod = Bukkit.getServer().getPlayer(received.get(0));
            String targetname = received.get(1);
            SanctionGUI.openModGui(mod, targetname);
            SanctionGUI.getModAndTarget().put(mod, targetname);

        }
        if(action.equalsIgnoreCase("MuteDataAdd")){
            String targetName = received.get(0);
            MuteData.addMute(targetName);
        }
        if(action.equalsIgnoreCase("ModModeratorAdd")){
            String modUUID = received.get(0);
            ModModerator.addMod(modUUID);
        }
        if(action.equalsIgnoreCase("MuteDataRemove")){
            String targetName = received.get(0);
            MuteData.removeMute(targetName);
        }
        if(action.equalsIgnoreCase("ModModeratorRemove")){
            String modUUID = received.get(0);
            ModModerator.removeMod(modUUID);
        }
        if(action.equalsIgnoreCase("SendFriendsDataRemove")){
            String playerName = received.get(0);
            String targetName = received.get(1);
            Player player1 = Bukkit.getPlayer(playerName);
            if(FriendsGUI.getDataFriends().containsKey(player1)){
                FriendsGUI.getDataFriends().get(player1).remove(targetName);
            }
        }
        if(action.equalsIgnoreCase("SendFriendsDataAdd")){
            String playerName = received.get(0);
            String targetName = received.get(1);
            Player player1 = Bukkit.getPlayer(playerName);
            if(FriendsGUI.getDataFriends().containsKey(player1)){
                FriendsGUI.getDataFriends().get(player1).add(targetName);
            }
            else
            {
                ArrayList<String> n = new ArrayList<>();
                n.add(targetName);
                FriendsGUI.getDataFriends().put(player1, n);
            }
        }
        if(action.equalsIgnoreCase("FriendsGUI")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            if(player1 != null){
                FriendsGUI.openFriendsGUI(player1);
            }
        }
        if(action.equalsIgnoreCase("OptionsGUI")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            if(player1 != null){
                Options.openOptionsInv(player1);
            }
        }
        if(action.equalsIgnoreCase("UpdateChatState")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            String state = received.get(1);
            if(player1 != null){
                Options.updateDataStateChat(player1, state);
            }
        }
        if(action.equalsIgnoreCase("UpdatePartyInviteState")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            String state = received.get(1);
            if(player1 != null){
                Options.updateDataStatePartyInvite(player1, state);
            }
        }
        if(action.equalsIgnoreCase("UpdateFriendRequestsState")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            String state = received.get(1);
            if(player1 != null){
                Options.updateDataStateFriendRequests(player1, state);
            }
        }
        if(action.equalsIgnoreCase("UpdateFriendsStatutNotifState")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            String state = received.get(1);
            if(player1 != null){
                Options.updateDataStateFriendsStatutNotif(player1, state);
            }
        }
        if(action.equalsIgnoreCase("UpdateMPState")){
            Player player1 = Bukkit.getPlayer(received.get(0));
            String state = received.get(1);
            if(player1 != null){
                Options.updateDataStateMP(player1, state);
            }
        }

    }



}
