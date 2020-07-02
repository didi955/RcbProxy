package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {


    public ReportCommand() {
        super("report");
    }

    private static String cmd = "report";

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!RcbProxy.getInstance().getMuteManager().isMuted(player.getUniqueId())){
                if(!(args.length < 1)){
                    if(!(args.length < 2)){
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                        if(target != null){
                            String raison = "";
                            for(int i=0; i < args.length; i++){
                                raison = raison+args[i] + " ";
                            }
                            for(ProxiedPlayer mods : ProxyServer.getInstance().getPlayers()){
                                if(mods.hasPermission("rcbproxy.report")){
                                    mods.sendMessage(new TextComponent("§c[REPORT] §b" + player.getName() + " §6a report §c" + target.getName() + " §6pour §e" +  raison));
                                }
                            }
                            player.sendMessage(new TextComponent("§aVous avez report §c" + target.getName()));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cCe joueur n'est pas connecté !"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVeuillez spécifier une raison !"));
                        player.sendMessage(new TextComponent("§c/report <joueur> <raison>"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§cVeuillez spécifier un joueur !"));
                    player.sendMessage(new TextComponent("§c/report <joueur> <raison>"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§cVous avez été mute !"));
            }
        }
    }

    public static String getCmd() {
        return cmd;
    }
}
