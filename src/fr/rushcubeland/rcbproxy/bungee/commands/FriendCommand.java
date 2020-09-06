package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.friends.Friend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class FriendCommand extends Command {


    private static final List<String> cmds = Arrays.asList("friend", "f", "friends", "ami", "amis");

    public FriendCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            Account account = RcbProxy.getInstance().getAccount(player);
            AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
            if(args.length == 0){
                infos(player);
                return;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("gui")){
                    Friend.openFriendsInventory(player);
                }
                else
                {
                    infos(player);
                    player.sendMessage(new TextComponent("§d[Amis] §cVeuillez spécifier un argument valide !"));
                }
            }
            else
            {
                if(args[0].equalsIgnoreCase("add")){
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if(target != player){
                        if(target != null){
                            Friend.sendFriendRequest(player, target);
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§d[Amis] §cCe joueur n'est pas connecté !"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Amis] §cImpossible d'envoyer une requête d'amis à vous meme !"));
                    }
                }
                if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("rm")){
                    if(!args[1].equals(player.getName())){
                        if(aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(args[1])))){
                            Friend.removeFriend(player, args[1]);
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§d[Amis] §cVous n'etes pas ami avec §e" + args[1]));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Amis] §cImpossible de vous supprimer de votre liste d'amis :)"));
                    }
                }
                if(args[0].equalsIgnoreCase("accept")){
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if(target != null){
                        Friend.acceptFriendRequest(player, target);
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Amis] §cCe joueur n'est pas connecté !"));
                    }
                }
                if(args[0].equalsIgnoreCase("deny")){
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                    if(target != null){
                        Friend.denyFriendRequest(player, target);
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Amis] §cCe joueur n'est pas connecté !"));
                    }
                }
            }
        }
    }

    private void infos(ProxiedPlayer player){
        player.sendMessage(new TextComponent("§e---------§d[Amis]§e---------"));
        player.sendMessage(new TextComponent("§6/friends list - §eAfficher la liste de vos amis"));
        player.sendMessage(new TextComponent("§6/friends add <joueur> - §eEnvoyer une requête d'ami"));
        player.sendMessage(new TextComponent("§6/friends remove <joueur> - §eRetirer un joueur de votre liste"));
        player.sendMessage(new TextComponent("§ed'amis"));
        player.sendMessage(new TextComponent("§6/friends accept <joueur> - §eAccepter la requête d'ami d'un"));
        player.sendMessage(new TextComponent("§ejoueur"));
        player.sendMessage(new TextComponent("§6/friends deny <joueur> - §eRefuser la requête d'ami d'un"));
        player.sendMessage(new TextComponent("§ejoueur"));
        player.sendMessage(new TextComponent("§e----------------------"));
    }

    public static List<String> getCmds() {
        return cmds;
    }
}
