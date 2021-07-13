package fr.rushcubeland.rcbproxy.bungee.parties;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.commons.options.OptionUnit;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.data.exceptions.AccountNotFoundException;
import fr.rushcubeland.rcbproxy.bungee.provider.AccountProvider;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Optional;

public class Parties {

    private static final HashMap<ProxiedPlayer, HashMap<ProxiedPlayer, Party>> datarequest = new HashMap<>();

    public static void leave(ProxiedPlayer player){
        Optional<AParty> optional = RcbProxy.getInstance().getAccountParty(player);
        if(optional.isPresent()){
            AParty aParty = optional.get();
            if(aParty.isInParty()){
                Party party = aParty.getParty();
                aParty.getParty().removePlayer(player);
                player.sendMessage(new TextComponent("§d[Groupe] §cVous avez quitté votre groupe !"));
                for(ProxiedPlayer pls : party.getPlayers()){
                    pls.sendMessage(new TextComponent("§d[Groupe] §b" + player.getDisplayName() + " §ca quitté le groupe !"));
                }
            }
        }
    }

    public static void removeMember(ProxiedPlayer player, ProxiedPlayer target){
        Optional<AParty> optional = RcbProxy.getInstance().getAccountParty(player);
        if(optional.isPresent()){
            AParty aParty = optional.get();
            if(aParty.getParty().getPlayers().contains(target)){

                try {

                    final AccountProvider accountProvider = new AccountProvider(target);
                    final Account account = accountProvider.getAccount();
                    aParty.getParty().removePlayer(target);
                    target.sendMessage(new TextComponent("§d[Groupe] §e" + account.getRank().getPrefix() + player.getDisplayName() + " §cvous a exclu de son groupe !"));
                    player.sendMessage(new TextComponent("§d[Groupe] §cVous avez retiré §e" + account.getRank().getPrefix() + target.getDisplayName() + " §cde votre groupe !"));

                } catch (AccountNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Groupe] §ce" + target.getDisplayName() + " §c n'est pas dans votre groupe !"));
            }
        }

    }

    public static void addMember(ProxiedPlayer player, ProxiedPlayer target){

        try {

            final AccountProvider accountProvider = new AccountProvider(player);
            final Account account = accountProvider.getAccount();
            final Account account2 = accountProvider.getAccount();

            Optional<AParty> optional = RcbProxy.getInstance().getAccountParty(player);
            if(optional.isPresent()){
                AParty aParty = optional.get();

                if(!aParty.isInParty()){
                    Party party = new Party(5);
                    party.addPlayer(player);
                    party.setCaptain(player);
                    aParty.getParty().addPlayer(target);
                    target.sendMessage(new TextComponent("§d[Groupe] §e" + account.getRank().getPrefix() + player.getDisplayName() + " §6vous a §aajouté §6à son groupe !"));
                    player.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aajouté §e" + account2.getRank().getPrefix() + target.getDisplayName() + " §6à votre groupe"));
                    return;

                }
                if(!aParty.getParty().getPlayers().contains(target)){
                    if(aParty.getParty().getPlayers().size() < aParty.getParty().getMaxPlayers()){
                        aParty.getParty().addPlayer(target);
                        target.sendMessage(new TextComponent("§d[Groupe] §e" + account.getRank().getPrefix() + player.getDisplayName() + " §6vous a §aajouté §6à son groupe !"));
                        player.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aajouté §e" + account2.getRank().getPrefix() + target.getDisplayName() + " §6à votre groupe"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Groupe] §cLa partie a atteint sa limite de joueurs !"));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponent("§d[Groupe] §e" + target.getDisplayName() + " §cest déjà dans votre groupe !"));
                }
            }

        } catch (AccountNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public static void sendRequest(ProxiedPlayer sender, ProxiedPlayer target){
        if(RcbProxy.getInstance().getAccountParty(sender).isEmpty() || RcbProxy.getInstance().getAccountParty(target).isEmpty()){
            return;
        }
        if(RcbProxy.getInstance().getAccountParty(sender).get().isInParty() && RcbProxy.getInstance().getAccountParty(target).get().isInParty()){
            if(!RcbProxy.getInstance().getAccountParty(sender).get().getParty().equals(RcbProxy.getInstance().getAccountParty(target).get().getParty())){
                if(RcbProxy.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.OPEN)){
                    sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + RcbProxy.getInstance().getAccount(target).getRank().getPrefix() + target.getName()));
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
                    ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                    TextComponent accept = new TextComponent("§a[Accepter]");
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + sender.getDisplayName()));
                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group accept " + sender.getDisplayName())));
                    TextComponent deny = new TextComponent("§c[Refuser]");
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group deny " + sender.getDisplayName()));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group deny " + sender.getDisplayName())));
                    componentBuilder.append(accept);
                    componentBuilder.append(new TextComponent("      "));
                    componentBuilder.append(deny);
                    target.sendMessage(componentBuilder.create());
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    HashMap<ProxiedPlayer, Party> h = new HashMap<>();
                    h.put(target, RcbProxy.getInstance().getAccountParty(sender).get().getParty());
                    datarequest.put(sender, h);
                }
                else if(RcbProxy.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.ONLY_FRIENDS) && RcbProxy.getInstance().getAccountFriends(sender).areFriendWith(target.getUniqueId())){
                    sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + RcbProxy.getInstance().getAccount(target).getRank().getPrefix() + target.getName()));
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
                    ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                    TextComponent accept = new TextComponent("§a[Accepter]");
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + sender.getDisplayName()));
                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group accept " + sender.getDisplayName())));
                    TextComponent deny = new TextComponent("§c[Refuser]");
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group deny " + sender.getDisplayName()));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group deny " + sender.getDisplayName())));
                    componentBuilder.append(accept);
                    componentBuilder.append(new TextComponent("      "));
                    componentBuilder.append(deny);
                    target.sendMessage(componentBuilder.create());
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    HashMap<ProxiedPlayer, Party> h = new HashMap<>();
                    h.put(target, RcbProxy.getInstance().getAccountParty(sender).get().getParty());
                    datarequest.put(sender, h);
                }
                else
                {
                    sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
                    sender.sendMessage(new TextComponent("§cgroupe !"));
                }
            }
            else
            {
                sender.sendMessage(new TextComponent("§d[Groupe] §cVous etes déjà dans le meme groupe !"));
            }
        }
        else if(RcbProxy.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.ONLY_FRIENDS)){
            if(RcbProxy.getInstance().getAccountFriends(sender).areFriendWith(target.getUniqueId())){
                if(!RcbProxy.getInstance().getAccountParty(sender).get().isInParty()){
                    Party party = new Party(5);
                    party.addPlayer(sender);
                    party.setCaptain(sender);
                    sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + RcbProxy.getInstance().getAccount(target).getRank().getPrefix() + target.getName()));
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
                    ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                    TextComponent accept = new TextComponent("§a[Accepter]");
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + sender.getName()));
                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group accept " + sender.getDisplayName())));
                    TextComponent deny = new TextComponent("§c[Refuser]");
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group deny " + sender.getName()));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group deny " + sender.getDisplayName())));
                    componentBuilder.append(accept);
                    componentBuilder.append(new TextComponent("      "));
                    componentBuilder.append(deny);
                    target.sendMessage(componentBuilder.create());
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    HashMap<ProxiedPlayer, Party> h = new HashMap<>();
                    h.put(target, RcbProxy.getInstance().getAccountParty(sender).get().getParty());
                    datarequest.put(sender, h);
                }
                else
                {
                    sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + RcbProxy.getInstance().getAccount(target).getRank().getPrefix() + target.getName()));
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
                    ComponentBuilder componentBuilder = new ComponentBuilder("      ");
                    TextComponent accept = new TextComponent("§a[Accepter]");
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + sender.getName()));
                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group accept " + sender.getDisplayName())));
                    TextComponent deny = new TextComponent("§c[Refuser]");
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group deny " + sender.getName()));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group deny " + sender.getDisplayName())));
                    componentBuilder.append(accept);
                    componentBuilder.append(new TextComponent("      "));
                    componentBuilder.append(deny);
                    target.sendMessage(componentBuilder.create());
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    HashMap<ProxiedPlayer, Party> h = new HashMap<>();
                    h.put(target, RcbProxy.getInstance().getAccountParty(sender).get().getParty());
                    datarequest.put(sender, h);
                }
            }
            else
            {
                sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
                sender.sendMessage(new TextComponent("§cgroupe !"));
            }
        }
        else if(RcbProxy.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.NEVER)){
            sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
            sender.sendMessage(new TextComponent("§cgroupe !"));
        }
        else if(RcbProxy.getInstance().getAccountOptions(target).getStatePartyInvite().equals(OptionUnit.OPEN)){
            Party party = new Party(5);
            party.addPlayer(sender);
            party.setCaptain(sender);
            sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + RcbProxy.getInstance().getAccount(target).getRank().getPrefix() + target.getName()));
            target.sendMessage(new TextComponent("§e-----------------------------"));
            target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
            ComponentBuilder componentBuilder = new ComponentBuilder("      ");
            TextComponent accept = new TextComponent("§a[Accepter]");
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group accept " + sender.getName()));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group accept " + sender.getDisplayName())));
            TextComponent deny = new TextComponent("§c[Refuser]");
            deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/group deny " + sender.getName()));
            deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,  TextComponent.fromLegacyText("/group deny " + sender.getDisplayName())));
            componentBuilder.append(accept);
            componentBuilder.append(new TextComponent("      "));
            componentBuilder.append(deny);
            target.sendMessage(componentBuilder.create());
            target.sendMessage(new TextComponent("§e-----------------------------"));
            HashMap<ProxiedPlayer, Party> h = new HashMap<>();
            h.put(target, RcbProxy.getInstance().getAccountParty(sender).get().getParty());
            datarequest.put(sender, h);
        }
        else
        {
            sender.sendMessage(new TextComponent("§d[Groupe] §cCe joueur ne souhaite pas recevoir d'invitations de"));
            sender.sendMessage(new TextComponent("§cgroupe !"));
        }
    }

    public static void denyRequest(ProxiedPlayer sender, ProxiedPlayer target, Party party){
        if(datarequest.containsKey(sender)){
            HashMap<ProxiedPlayer, Party> value = datarequest.get(sender);
            if(value.containsKey(target)){
                if(value.get(target).equals(party)){
                    datarequest.get(sender).remove(target);
                    target.sendMessage(new TextComponent("§d[Groupe] §cVous avez décliné l'invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getName()));
                }
            }
        }
        else
        {
            target.sendMessage(new TextComponent("§d[Groupe] §cRequête introuvable !"));
        }
    }

    public static void acceptRequest(ProxiedPlayer sender, ProxiedPlayer target, Party party){
        if(datarequest.containsKey(sender)){
            HashMap<ProxiedPlayer, Party> value = datarequest.get(sender);
            if(value.containsKey(target)){
                if(value.get(target).equals(party)){
                    datarequest.get(sender).remove(target);
                    addMember(sender, target);
                    target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aaccepté §6l'invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).getRank().getPrefix() + sender.getDisplayName()));
                }
            }
        }
        else
        {
            target.sendMessage(new TextComponent("§d[Groupe] §cRequête introuvable !"));
        }
    }

    public static HashMap<ProxiedPlayer, HashMap<ProxiedPlayer, Party>> getDatarequest() {
        return datarequest;
    }
}
