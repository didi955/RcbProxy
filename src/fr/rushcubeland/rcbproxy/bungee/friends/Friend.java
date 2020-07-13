package fr.rushcubeland.rcbproxy.bungee.friends;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.database.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.database.MySQL;
import fr.rushcubeland.rcbproxy.bungee.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Friend {

    private static final HashMap<ProxiedPlayer, ArrayList<ProxiedPlayer>> datarequest = new HashMap<>();

    public static void removeFriend(ProxiedPlayer player, String targetName){
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            if(account.get().getDataFriends().areFriendWith(targetName)){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
                if(target != null){
                    Optional<Account> account2 = RcbProxy.getInstance().getAccount(target);
                    if(account2.isPresent()){
                        account.get().getDataFriends().removeFriend(target.getName());
                        account2.get().getDataFriends().removeFriend(player.getName());
                        BungeeSend.sendFriendsDataRemove(player, targetName);
                        BungeeSend.sendFriendsDataRemove(target, player.getName());
                        target.sendMessage(new TextComponent("§d[Amis] §e" + account.get().getDatarank().getRank().getPrefix() + player.getName() + " §cvous a retiré de sa liste d'amis !"));
                        player.sendMessage(new TextComponent("§d[Amis] §cVous avez retiré §e" + account2.get().getDatarank().getRank().getPrefix() + target.getName() + " §cde votre liste d'amis !"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                        player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                    }
                }
                else
                {
                    account.get().getDataFriends().removeFriend(targetName);
                    BungeeSend.sendFriendsDataRemove(player, targetName);
                    forceRemoveOfflineFriend(player, targetName);
                    player.sendMessage(new TextComponent("§d[Amis] §cVous avez §cretiré §e" + targetName + " §cde votre liste d'amis !"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Amis] §cVous n'etes pas ami avec §e" + targetName + " §c!"));
            }
        }
    }

    public static void addFriend(ProxiedPlayer player, String targetName){
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            if(!account.get().getDataFriends().areFriendWith(targetName)){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
                if(target != null){
                    Optional<Account> account2 = RcbProxy.getInstance().getAccount(target);
                    if(account2.isPresent()){
                        if(!account.get().getDataFriends().hasReachedMaxFriends() || !account2.get().getDataFriends().hasReachedMaxFriends()){
                            account.get().getDataFriends().addFriend(target.getName());
                            account2.get().getDataFriends().addFriend(player.getName());
                            BungeeSend.sendFriendsDataAdd(player, targetName);
                            BungeeSend.sendFriendsDataAdd(target, player.getName());
                            target.sendMessage(new TextComponent("§d[Amis] §e" + account.get().getDatarank().getRank().getPrefix() + player.getName() + " §6vous a §aajouté §6à sa liste d'amis !"));
                            player.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aajouté §e" + account2.get().getDatarank().getRank().getPrefix() + target.getName() + " §6de votre liste d'amis !"));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§d[Amis] §cVous ou le joueur cible a atteint le nombre maximum d'amis !"));
                        }
                    }
                    else
                    {
                        account.get().getDataFriends().addFriend(targetName);
                        player.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aajouté §e" + targetName + " §6de votre liste d'amis !"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§f[Amis] §cCe joueur n'est pas connecté !"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Amis] §cVous etes déjà ami avec §e" + targetName + " §c!"));
            }
        }
    }

    public static void denyFriendRequest(ProxiedPlayer receiver, ProxiedPlayer sender){
        if(datarequest.containsKey(sender)){
            if(datarequest.get(sender).contains(receiver)){
                datarequest.get(sender).remove(receiver);
                if(RcbProxy.getInstance().getAccount(sender).isPresent()){
                    receiver.sendMessage(new TextComponent("§d[Amis] §cVous avez décliné la requête d'ami de " + RcbProxy.getInstance().getAccount(sender).get().getDatarank().getRank().getPrefix()+sender.getName()));
                }
                else
                {
                    receiver.sendMessage(new TextComponent("§d[Amis] §cVous avez décliné la requête d'ami de " + sender.getName()));
                }
            }
        }
        else
        {
            receiver.sendMessage(new TextComponent("§d[Amis] §cRequête introuvable !"));
        }
    }

    public static void acceptFriendRequest(ProxiedPlayer receiver, ProxiedPlayer sender){
        if(datarequest.containsKey(sender)){
            if(datarequest.get(sender).contains(receiver)){
                datarequest.get(sender).remove(receiver);
                if(RcbProxy.getInstance().getAccount(sender).isPresent()){
                    addFriend(receiver, sender.getName());
                    receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aaccepté §6la requête d'ami de " + RcbProxy.getInstance().getAccount(sender).get().getDatarank().getRank().getPrefix() + sender.getName()));
                }
            }
        }
        else
        {
            receiver.sendMessage(new TextComponent("§d[Amis] §cRequête introuvable !"));
        }
    }

    public static void sendFriendRequest(ProxiedPlayer sender, ProxiedPlayer receiver){
        Optional<Account> account = RcbProxy.getInstance().getAccount(sender);
        Optional<Account> account2 = RcbProxy.getInstance().getAccount(receiver);
        if(account.isPresent() && account2.isPresent()){
            if(!account.get().getDataFriends().areFriendWith(receiver.getName())){
                if(account2.get().getDataOptions().getStateFriendRequests().equals(OptionUnit.OPEN)){
                    if(datarequest.containsKey(sender)){
                        if(!(datarequest.get(sender).contains(receiver))){
                            sender.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aenvoyé §6une requête d'ami à " + account2.get().getDatarank().getRank().getPrefix() + receiver.getName()));
                            receiver.sendMessage(new TextComponent("§e-----------------------------"));
                            receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §arecu §6une requête d'ami de " + account.get().getDatarank().getRank().getPrefix() + sender.getName()));
                            datarequest.get(sender).add(receiver);
                            ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                            TextComponent accept = new TextComponent("§a[Accepter]");
                            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + sender.getName()));
                            TextComponent deny = new TextComponent("§c[Refuser]");
                            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny " + sender.getName()));
                            componentBuilder.append(accept);
                            componentBuilder.append(new TextComponent("      "));
                            componentBuilder.append(deny);
                            receiver.sendMessage(componentBuilder.create());
                            receiver.sendMessage(new TextComponent("§e-----------------------------"));
                        }
                        else
                        {
                            sender.sendMessage(new TextComponent("§d[Amis] §cVous avez déjà envoyé une requête d'ami à ce joueur !"));
                        }
                    }
                    else
                    {
                        sender.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aenvoyé §6une requête d'ami à " + account2.get().getDatarank().getRank().getPrefix() + receiver.getName()));
                        receiver.sendMessage(new TextComponent("§e-----------------------------"));
                        receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §arecu §6une requête d'ami de " + account.get().getDatarank().getRank().getPrefix() + sender.getName()));
                        ArrayList<ProxiedPlayer> nouvelle = new ArrayList<>();
                        nouvelle.add(receiver);
                        datarequest.put(sender, nouvelle);
                        ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                        TextComponent accept = new TextComponent("§a[Accepter]");
                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept " + sender.getName()));
                        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("/f accept " + sender.getName())));
                        TextComponent deny = new TextComponent("§c[Refuser]");
                        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny " + sender.getName()));
                        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("/f deny " + sender.getName())));
                        componentBuilder.append(accept);
                        componentBuilder.append(new TextComponent("      "));
                        componentBuilder.append(deny);
                        receiver.sendMessage(componentBuilder.create());
                        receiver.sendMessage(new TextComponent("§e-----------------------------"));
                    }
                }
                else
                {
                    sender.sendMessage(new TextComponent("§d[Amis] §cCe joueur ne souhaite pas recevoir des requêtes d'amis !"));
                }
            }
            else
            {
                sender.sendMessage(new TextComponent("§d[Amis] §cVous etes déjà ami avec " + account2.get().getDatarank().getRank().getPrefix() + receiver.getName() + " §c!"));
            }
        }
    }

    public static void openFriendsInventory(ProxiedPlayer player){
        BungeeSend.sendGUIFriends(player);
    }

    private static void forceRemoveOfflineFriend(ProxiedPlayer player, String targetName){
        String uuid = UUIDFetcher.getUUIDFromName(targetName);
        if(uuid != null){
            try {
                MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM Proxyplayer_friends WHERE uuid='%s' AND friend='%s'",
                uuid, player.getName()));
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void quitNotifFriends(ProxiedPlayer player){
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            for(String friend : account.get().getDataFriends().getFriends()){
                ProxiedPlayer friendP = ProxyServer.getInstance().getPlayer(friend);
                if(friendP != null){
                    Optional<Account> account2 = RcbProxy.getInstance().getAccount(friendP);
                    if(account2.isPresent()){
                        if(account2.get().getDataOptions().getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                            friendP.sendMessage(new TextComponent("§d[Ami] §b" + player.getName() + " §cs'est déconnecté !"));
                        }
                    }
                }
            }
        }
    }

    public static void joinNotifFriends(ProxiedPlayer player){
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            for(String friend : account.get().getDataFriends().getFriends()){
                ProxiedPlayer friendP = ProxyServer.getInstance().getPlayer(friend);
                if(friendP != null){
                    Optional<Account> account2 = RcbProxy.getInstance().getAccount(friendP);
                    if(account2.isPresent()){
                        if(account2.get().getDataOptions().getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                            friendP.sendMessage(new TextComponent("§d[Ami] §b" + player.getName() + " §as'est connecté !"));
                        }
                    }
                }
            }
        }
    }

    public static HashMap<ProxiedPlayer, ArrayList<ProxiedPlayer>> getDatarequest() {
        return datarequest;
    }

    public static void onQuit(ProxiedPlayer player){
        datarequest.remove(player);
    }
}
