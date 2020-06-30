package fr.rushcubeland.rcbproxy.bukkit;

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
    }



}
