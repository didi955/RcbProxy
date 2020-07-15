package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;

public class OptionsCommand extends Command {

    private static final List<String> cmds = Arrays.asList("options", "opt", "option");

    public OptionsCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(args.length == 0){
                BungeeSend.sendGUIOptions(player);
            }
        }
    }

    public static List<String> getCmds() {
        return cmds;
    }
}
