package fr.rushcubeland.rcbproxy.bungee.listeners;

import fr.rushcubeland.commons.AFriends;
import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.APermissions;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.rcbproxy.bungee.provider.AccountProvider;
import fr.rushcubeland.rcbproxy.bungee.provider.FriendsProvider;
import fr.rushcubeland.rcbproxy.bungee.provider.OptionsProvider;
import fr.rushcubeland.rcbproxy.bungee.provider.PermissionsProvider;
import fr.rushcubeland.rcbproxy.bungee.rank.RankUnit;
import fr.rushcubeland.rcbproxy.bungee.friends.Friend;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxiedPlayerJoin implements Listener {

    @EventHandler
    public void onConnect(PostLoginEvent e){
        ProxiedPlayer player = e.getPlayer();

        initAllDataProvider(player);

        Account account = RcbProxy.getInstance().getAccount(player);
        account.setState(true);
        RcbProxy.getInstance().sendAccountToRedis(account);

        Friend.joinNotifFriends(player);

        initPermissions(player, RcbProxy.getInstance().getAccount(player).getRank());
    }

    @EventHandler
    public void onLogin(LoginEvent e){
        RcbProxy.getInstance().getBanManager().checkDuration(e.getConnection().getUniqueId());

        if(RcbProxy.getInstance().getBanManager().isBanned(e.getConnection().getUniqueId())) {
            e.setCancelled(true);
            e.setCancelReason(new TextComponent("§cVous avez été banni !\n \n§eRaison : §f" +
                    RcbProxy.getInstance().getBanManager().getReason(e.getConnection().getUniqueId()) + "\n \n §aTemps restant : §f" +
                    RcbProxy.getInstance().getBanManager().getTimeLeft(e.getConnection().getUniqueId())));
        }
    }

    private void initPermissions(ProxiedPlayer player, RankUnit rank){
        initPlayerPermissions(player);
        initRankPlayerPermissions(player, rank);
    }

    private void initRankPlayerPermissions(ProxiedPlayer player, RankUnit rank){
        if(rank.getPermissions().isEmpty()){
            return;
        }
        for(String perm : rank.getPermissions()){
            player.setPermission(perm, true);
        }
    }

    private void initPlayerPermissions(ProxiedPlayer player){
        APermissions aPermissions = RcbProxy.getInstance().getAccountPermissions(player);
        for(String perm : aPermissions.getPermissions()){
            player.setPermission(perm, true);
        }
    }

    private void initAllDataProvider(ProxiedPlayer player){

        AParty aParty = new AParty(player.getUniqueId());
        RcbProxy.getInstance().getAPartyList().add(aParty);

        try {

            final AccountProvider accountProvider = new AccountProvider(player);
            accountProvider.getAccount();

        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
            player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
        }

        try {

            final OptionsProvider optionsProvider = new OptionsProvider(player);
            optionsProvider.getAccount();

        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
            player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
        }

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

        try {

            final PermissionsProvider permissionsProvider = new PermissionsProvider(player);
            permissionsProvider.getAccount();


        } catch (AccountNotFoundException exception) {
            System.err.println(exception.getMessage());
            player.disconnect(new TextComponent("§cVotre compte n'a pas été crée ou trouvé !"));
        }

    }
}
