package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.network.Network;
import fr.rushcubeland.rcbproxy.bungee.network.ServerGroup;
import fr.rushcubeland.rcbproxy.bungee.network.ServerUnit;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class HubCommand extends Command {

    private static final List<String> cmds = Arrays.asList("hub", "lobby");

    public HubCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            Network.JoinLobby(player);
        }
    }

    public static List<String> getCmds() {
        return cmds;
    }
}
