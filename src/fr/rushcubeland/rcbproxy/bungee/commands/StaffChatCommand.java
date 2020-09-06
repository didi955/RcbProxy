package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command {

    public StaffChatCommand() {
        super("staff");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(player.hasPermission("rcbproxy.staffchat")){
                if(args.length > 0){
                    String message = "";
                    for(int i=0; i < args.length; i++){
                        message = message+args[i] + " ";
                    }
                    Account account = RcbProxy.getInstance().getAccount(player);
                    for(ProxiedPlayer plsstaff : ProxyServer.getInstance().getPlayers()){
                        if(plsstaff.hasPermission("rcbproxy.staffchat")){
                            plsstaff.sendMessage(new TextComponent("§6[StaffChat] " + account.getRank().getPrefix() + player.getName() + " §f: " + message));
                        }
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§c/staff <message>"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            }
        }

    }
}
