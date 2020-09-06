package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MPCommand extends Command {

    private static final String cmd = "msg";


    public MPCommand() {
        super("msg");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(RcbProxy.getInstance().getMuteManager().isMuted(player.getUniqueId())){
                player.sendMessage(new TextComponent("§cVous avez été mute !"));
                return;
            }
            if(args.length == 0){
                player.sendMessage(new TextComponent("§cVeuillez spécifier un joueur et votre message !"));
                player.sendMessage(new TextComponent("§c/msg <joueur> <message>"));
            }
            if(args.length == 1){
                player.sendMessage(new TextComponent("§cVeuillez spécifier votre message !"));
                player.sendMessage(new TextComponent("§c/msg <joueur> <message>"));
            }
            if(args.length >= 2){
                if(!args[0].equals(player.getDisplayName())){
                    ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(args[0]);
                    if(receiver != null){
                        String message = "";
                        for (int i = 1; i < args.length; i++) {
                            message = message + args[i] + " ";
                        }
                        Account account = RcbProxy.getInstance().getAccount(player);
                        Account account2 = RcbProxy.getInstance().getAccount(receiver);
                        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
                        AOptions aOptions1 = RcbProxy.getInstance().getAccountOptions(receiver);
                        if(aOptions1.getStateMP().equals(OptionUnit.NEVER)){
                            player.sendMessage(new TextComponent("§cCe joueur ne souhaite pas recevoir de messages privés !"));
                            return;
                        }
                        else if(aOptions1.getStateMP().equals(OptionUnit.ONLY_FRIENDS) && !aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(receiver.getName())))){
                            player.sendMessage(new TextComponent("§cCe joueur ne souhaite pas recevoir de messages privés !"));
                            return;
                        }
                        receiver.sendMessage(new TextComponent(account.getRank().getPrefix() + player.getDisplayName() + " §6-> §7Moi: §f" + message));
                        player.sendMessage(new TextComponent("§7Moi §6-> " + account2.getRank().getPrefix() + receiver.getDisplayName() + " §7: §f" + message));
                        RcbProxy.getInstance().getMpData().put(player, receiver);
                        RcbProxy.getInstance().getMpData().put(receiver, player);
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cCe joueur n'est pas connecté !"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§cVous ne pouvez pas envoyer un message à vous-meme !"));
                }
            }
        }
    }

    public static String getCmd() {
        return cmd;
    }
}
