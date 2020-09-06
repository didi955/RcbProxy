package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ReplyCommand extends Command {

    private static final List<String> cmds = Arrays.asList("reply", "r");

    public ReplyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(RcbProxy.getInstance().getMuteManager().isMuted(player.getUniqueId())){
                player.sendMessage(new TextComponent("§cVous avez été mute !"));
                return;
            }
            if(args.length >= 1){
                if(RcbProxy.getInstance().getMpData().containsKey(player)){
                    ProxiedPlayer target = RcbProxy.getInstance().getMpData().get(player);
                    if(target != null){
                        String message = "";
                        for (int i = 0; i < args.length; i++) {
                            message = message + args[i] + " ";
                        }
                        Account account = RcbProxy.getInstance().getAccount(player);
                        Account account2 = RcbProxy.getInstance().getAccount(target);
                        AOptions aOptions2 = RcbProxy.getInstance().getAccountOptions(target);
                        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
                        if(aOptions2.getStateMP().equals(OptionUnit.NEVER)){
                            player.sendMessage(new TextComponent("§cCe joueur ne souhaite pas recevoir de messages privés !"));
                            return;
                        }
                        else if(aOptions2.getStateMP().equals(OptionUnit.ONLY_FRIENDS) && !aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(target.getName())))){
                            player.sendMessage(new TextComponent("§cCe joueur ne souhaite pas recevoir de messages privés !"));
                            return;
                        }
                        target.sendMessage(new TextComponent(account.getRank().getPrefix() + player.getDisplayName() + " §6-> §7Moi: §f" + message));
                        player.sendMessage(new TextComponent("§7Moi §6-> " + account2.getRank().getPrefix() + target.getDisplayName() + " §7: §f" + message));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cLa personne n'est pas en-ligne !"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§cVous n'avez personne à qui répondre !"));
                }
            }
        }
    }

    public static List<String> getCmds() {
        return cmds;
    }
}
