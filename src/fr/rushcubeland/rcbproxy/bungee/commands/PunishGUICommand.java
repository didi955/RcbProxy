package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PunishGUICommand extends Command {

    private static String cmd = "ap";

    public PunishGUICommand() {
        super("ap");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission("rcbproxy.ap")){
                sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
                return;
            }
            if(args.length < 1){
                player.sendMessage(new TextComponent("§c/ap <joueur>"));
                return;

            }
            if(args.length == 1){
                String targetuuid = UUIDFetcher.getUUIDFromName(args[0]);
                if(targetuuid == null){
                    player.sendMessage(new TextComponent("§cCe joueur n'exsiste pas !"));
                    return;
                }
                BungeeSend.sendGuiPunishment(player, args[0]);
            }

        }
    }

    public static String getCmd() {
        return cmd;
    }
}
