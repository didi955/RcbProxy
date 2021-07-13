package fr.rushcubeland.rcbproxy.bungee.friends;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AOptions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.data.redis.RedisAccess;
import fr.rushcubeland.rcbproxy.bungee.data.sql.DatabaseManager;
import fr.rushcubeland.rcbproxy.bungee.data.sql.MySQL;
import fr.rushcubeland.rcbproxy.bungee.provider.FriendsProvider;
import fr.rushcubeland.rcbproxy.bungee.rank.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.sql.SQLException;
import java.util.*;

public class Friend {

    private static final HashMap<ProxiedPlayer, ArrayList<ProxiedPlayer>> datarequest = new HashMap<>();

    public static void removeFriend(ProxiedPlayer player, String targetName){
        Account account = RcbProxy.getInstance().getAccount(player);
        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
        if(aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(targetName)))){
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if(target != null){
                Account account2 = RcbProxy.getInstance().getAccount(target);
                AFriends aFriends2 = RcbProxy.getInstance().getAccountFriends(target);
                aFriends2.removeFriend(player.getUniqueId());
                aFriends.removeFriend(target.getUniqueId());
                target.sendMessage(new TextComponent("§d[Amis] §e" + account.getRank().getPrefix() + player.getName() + " §cvous a retiré de sa liste d'amis !"));
                player.sendMessage(new TextComponent("§d[Amis] §cVous avez retiré §e" + account2.getRank().getPrefix() + target.getName() + " §cde votre liste d'amis !"));
                final FriendsProvider friendsProvider = new FriendsProvider(target);
                friendsProvider.sendFriendsToRedis(aFriends2);
            }
            else
            {
                aFriends.removeFriend(UUID.fromString(UUIDFetcher.getUUIDFromName(targetName)));
                forceRemoveOfflineFriend(player, targetName);
                player.sendMessage(new TextComponent("§d[Amis] §cVous avez §cretiré §e" + targetName + " §cde votre liste d'amis !"));
            }
            final FriendsProvider friendsProvider = new FriendsProvider(player);
            friendsProvider.sendFriendsToRedis(aFriends);
        }
        else
        {
            player.sendMessage(new TextComponent("§d[Amis] §cVous n'etes pas ami avec §e" + targetName + " §c!"));
        }
    }

    public static void addFriend(ProxiedPlayer player, String targetName){
        Account account = RcbProxy.getInstance().getAccount(player);
        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
        if(!aFriends.areFriendWith(UUID.fromString(UUIDFetcher.getUUIDFromName(targetName)))){
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
            if(target != null){
                Account account2 = RcbProxy.getInstance().getAccount(target);
                AFriends aFriends2 = RcbProxy.getInstance().getAccountFriends(target);
                if(aFriends.hasReachedMaxFriends() || aFriends2.hasReachedMaxFriends()){
                    aFriends.addFriend(UUID.fromString(UUIDFetcher.getUUIDFromName(targetName)));
                    aFriends2.addFriend(player.getUniqueId());
                    target.sendMessage(new TextComponent("§d[Amis] §e" + account.getRank().getPrefix() + player.getName() + " §6vous a §aajouté §6à sa liste d'amis !"));
                    player.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aajouté §e" + account2.getRank().getPrefix() + target.getName() + " §6de votre liste d'amis !"));

                    final FriendsProvider friendsProvider = new FriendsProvider(player);
                    friendsProvider.sendFriendsToRedis(aFriends);
                    final FriendsProvider friendsProvider2 = new FriendsProvider(target);
                    friendsProvider2.sendFriendsToRedis(aFriends2);
                }
                else
                {
                    player.sendMessage(new TextComponent("§d[Amis] §cVous ou le joueur cible a atteint le nombre maximum d'amis !"));
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

    public static void denyFriendRequest(ProxiedPlayer receiver, ProxiedPlayer sender){
        if(datarequest.containsKey(sender)){
            if(datarequest.get(sender).contains(receiver)){
                datarequest.get(sender).remove(receiver);
                receiver.sendMessage(new TextComponent("§d[Amis] §cVous avez décliné la requête d'ami de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix()+sender.getName()));
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
                addFriend(receiver, sender.getName());
                receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aaccepté §6la requête d'ami de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
            }
        }
        else
        {
            receiver.sendMessage(new TextComponent("§d[Amis] §cRequête introuvable !"));
        }
    }

    public static void sendFriendRequest(ProxiedPlayer sender, ProxiedPlayer receiver){
        Account account = RcbProxy.getInstance().getAccount(sender);
        Account account2 = RcbProxy.getInstance().getAccount(receiver);
        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(sender);
        AOptions aOptions52 = RcbProxy.getInstance().getAccountOptions(receiver);
        if(!aFriends.areFriendWith(receiver.getUniqueId())){
            if(aOptions52.getStateFriendRequests().equals(OptionUnit.OPEN)){
                if(datarequest.containsKey(sender)){
                    if(!(datarequest.get(sender).contains(receiver))){
                        sender.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aenvoyé §6une requête d'ami à " + account2.getRank().getPrefix() + receiver.getName()));
                        receiver.sendMessage(new TextComponent("§e-----------------------------"));
                        receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §arecu §6une requête d'ami de " + account.getRank().getPrefix() + sender.getName()));
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
                    sender.sendMessage(new TextComponent("§d[Amis] §6Vous avez §aenvoyé §6une requête d'ami à " + account2.getRank().getPrefix() + receiver.getName()));
                    receiver.sendMessage(new TextComponent("§e-----------------------------"));
                    receiver.sendMessage(new TextComponent("§d[Amis] §6Vous avez §arecu §6une requête d'ami de " + account.getRank().getPrefix() + sender.getName()));
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
                sender.sendMessage(new TextComponent("§d[Amis] §cCe joueur ne souhaite pas recevoir des requêtes"));
                sender.sendMessage(new TextComponent("§fd'amis !"));
            }
        }
        else
        {
            sender.sendMessage(new TextComponent("§d[Amis] §cVous etes déjà ami avec " + account2.getRank().getPrefix() + receiver.getName() + " §c!"));
        }
    }

    public static void openFriendsInventory(ProxiedPlayer player){
        BungeeSend.sendGUIFriends(player);
    }

    private static void forceRemoveOfflineFriend(ProxiedPlayer player, String targetName){
        String uuid = UUIDFetcher.getUUIDFromName(targetName);
        if(uuid != null){
            final RedissonClient redissonClient = RedisAccess.INSTANCE.getRedissonClient();
            final String key = FriendsProvider.REDIS_KEY + uuid;
            final RBucket<AFriends> accountRBucket = redissonClient.getBucket(key);

            AFriends aFriends=  accountRBucket.get();
            if(aFriends == null){
                try {
                    MySQL.update(DatabaseManager.Main_BDD.getDatabaseAccess().getConnection(), String.format("DELETE FROM Friends WHERE uuid='%s' AND friend='%s'",
                            player.getUniqueId().toString(), uuid));
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
            else
            {
                List<UUID> friends = aFriends.getFriends();
                for(UUID f : friends){
                    if(UUID.fromString(uuid).equals(f)){
                        friends.remove(f);
                        aFriends.setFriends(friends);
                        final FriendsProvider accountProvider = new FriendsProvider(player);
                        accountProvider.sendFriendsToRedis(aFriends);
                    }
                }
            }
        }
    }

    public static void quitNotifFriends(ProxiedPlayer player){
        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
        for(UUID friend : aFriends.getFriends()){
            ProxiedPlayer friendP = ProxyServer.getInstance().getPlayer(friend);
            if(friendP != null){
                AOptions aOptions52 = RcbProxy.getInstance().getAccountOptions(friendP);
                if(aOptions52.getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                    RankUnit rank = RcbProxy.getInstance().getAccount(player).getRank();
                    friendP.sendMessage(new TextComponent("§d[Ami] §b" + rank.getPrefix() + player.getName() + " §cs'est déconnecté !"));
                }
            }
        }
    }

    public static void joinNotifFriends(ProxiedPlayer player){
        AFriends aFriends = RcbProxy.getInstance().getAccountFriends(player);
        for(UUID friend : aFriends.getFriends()){
            ProxiedPlayer friendP = ProxyServer.getInstance().getPlayer(friend);
            if(friendP != null){
                AOptions aOptions52 = RcbProxy.getInstance().getAccountOptions(friendP);
                if(aOptions52.getStateFriendsStatutNotif().equals(OptionUnit.OPEN)){
                    RankUnit rank = RcbProxy.getInstance().getAccount(player).getRank();
                    friendP.sendMessage(new TextComponent("§d[Ami] §b" + rank.getPrefix() + player.getName() + " §as'est connecté !"));
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
