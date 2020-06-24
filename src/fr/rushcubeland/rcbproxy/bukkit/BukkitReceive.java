package fr.rushcubeland.rcbproxy.bukkit;

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
        if (action.equalsIgnoreCase("teleport")) {
            Player from = Bukkit.getServer().getPlayer((String)received.get(0));
            Player to = Bukkit.getServer().getPlayer((String)received.get(1));
            from.teleport(to);
        }
    }

}
