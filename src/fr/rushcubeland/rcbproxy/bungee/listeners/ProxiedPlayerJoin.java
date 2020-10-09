package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.exceptions.AccountNotFoundException;
import fr.rushcubeland.rcbproxy.bungee.provider.AccountProvider;
import fr.rushcubeland.rcbproxy.bungee.provider.FriendsProvider;
import fr.rushcubeland.rcbproxy.bungee.provider.OptionsProvider;
import fr.rushcubeland.rcbproxy.bungee.rank.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.friends.Friend;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ProxiedPlayerJoin implements Listener {

    @EventHandler
    public void onConnect(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();

        initAllDataProvider(player);

        Friend.joinNotifFriends(player);

        for(UUID uuid : RcbProxy.getInstance().getAccountFriends(player).getFriends()){
            player.sendMessage(new TextComponent(uuid.toString() + ", "));
        }
    }

    @EventHandler
    public void onLogin(LoginEvent e){
        RcbProxy.getInstance().getBanManager().checkDuration(e.getConnection().getUniqueId());

        if(RcbProxy.getInstance().getBanManager().isBanned(e.getConnection().getUniqueId())) {
            e.setCancelled(true);
            e.setCancelReason(new TextComponent("§cVous avez été banni !\n \n §ePar: §b" + RcbProxy.getInstance().getBanManager().getModerator(e.getConnection().getUniqueId()) + " \n \n§eRaison : §f" +
                    RcbProxy.getInstance().getBanManager().getReason(e.getConnection().getUniqueId()) + "\n \n §aTemps restant : §f" +
                    RcbProxy.getInstance().getBanManager().getTimeLeft(e.getConnection().getUniqueId())));
        }
    }

    private void initRankPlayerPermissions(ProxiedPlayer player, RankUnit rank){
        if(rank.getPermissions().isEmpty()){
            return;
        }
        for(String perm : rank.getPermissions()){
            player.setPermission(perm, true);
        }
    }

    private void initAllDataProvider(ProxiedPlayer player){
        BungeeCord.getInstance().getScheduler().runAsync(RcbProxy.getInstance(), () -> {

            try {

                final AccountProvider accountProvider = new AccountProvider(player);
                accountProvider.getAccount();

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }
        });

        BungeeCord.getInstance().getScheduler().runAsync(RcbProxy.getInstance(), () -> {

            try {

                final OptionsProvider optionsProvider = new OptionsProvider(player);
                optionsProvider.getAccount();

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }
        });

        BungeeCord.getInstance().getScheduler().runAsync(RcbProxy.getInstance(), () -> {

            try {

                final FriendsProvider friendsProvider = new FriendsProvider(player);
                AFriends aFriends = friendsProvider.getAccount();

                final AccountProvider accountProvider = new AccountProvider(player);
                Account account = accountProvider.getAccount();

                if(account.getRank().getPower() == 45){
                    aFriends.setMaxFriends(30);
                }
                if(account.getRank().getPower() == 40){
                    aFriends.setMaxFriends(40);
                }
                if(account.getRank().getPower() == 38 || account.getRank().getPower() == 37){
                    aFriends.setMaxFriends(50);
                }
                if(account.getRank().getPower() < 37){
                    aFriends.setMaxFriends(50);
                }

                friendsProvider.sendFriendsToRedis(aFriends);

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
                player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
            }
        });

    }

}
