package fr.rushcubeland.rcbproxy.bungee.parties;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
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
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            if(account.get().getDataParty().isInParty()){
                Party party = account.get().getDataParty().getParty();
                account.get().getDataParty().getParty().removePlayer(player);
                player.sendMessage(new TextComponent("§d[Groupe] §cVous avez quitté votre groupe !"));
                for(ProxiedPlayer pls : party.getPlayers()){
                    pls.sendMessage(new TextComponent("§d[Groupe] §b" + player.getDisplayName() + " §ca quitté le groupe !"));
                }
            }
        }
    }

    public static void removeMember(ProxiedPlayer player, ProxiedPlayer target){
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if(account.isPresent()){
            if(account.get().getDataParty().getParty().getPlayers().contains(target)){
                Optional<Account> account2 = RcbProxy.getInstance().getAccount(target);
                if(account2.isPresent()){
                    account.get().getDataParty().getParty().removePlayer(target);
                    target.sendMessage(new TextComponent("§d[Groupe] §e" + account.get().getDatarank().getRank().getPrefix() + player.getDisplayName() + " §cvous a exclu de son groupe !"));
                    player.sendMessage(new TextComponent("§d[Groupe] §cVous avez retiré §e" + account2.get().getDatarank().getRank().getPrefix() + target.getDisplayName() + " §cde votre groupe !"));
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Groupe] §ce" + target.getDisplayName() + " §c n'est pas dans votre groupe !"));
            }
        }
    }

    public static void addMember(ProxiedPlayer player, ProxiedPlayer target){
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        Optional<Account> account2 = RcbProxy.getInstance().getAccount(target);
        if(account.isPresent()){
            if(!account.get().getDataParty().isInParty()){
                Party party = new Party(5);
                party.addPlayer(player);
                party.setCaptain(player);
                account.get().getDataParty().getParty().addPlayer(target);
                target.sendMessage(new TextComponent("§d[Groupe] §e" + account.get().getDatarank().getRank().getPrefix() + player.getDisplayName() + " §6vous a §aajouté §6à son groupe !"));
                player.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aajouté §e" + account2.get().getDatarank().getRank().getPrefix() + target.getDisplayName() + " §6à votre groupe"));
                return;

            }
            if(!account.get().getDataParty().getParty().getPlayers().contains(target)){
                if(account2.isPresent()){
                    if(account.get().getDataParty().getParty().getPlayers().size() < account.get().getDataParty().getParty().getMaxPlayers()){
                        account.get().getDataParty().getParty().addPlayer(target);
                        target.sendMessage(new TextComponent("§d[Groupe] §e" + account.get().getDatarank().getRank().getPrefix() + player.getDisplayName() + " §6vous a §aajouté §6à son groupe !"));
                        player.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aajouté §e" + account2.get().getDatarank().getRank().getPrefix() + target.getDisplayName() + " §6à votre groupe"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§d[Groupe] §cLa partie a atteint sa limite de joueurs !"));
                    }
                }
            }
            else
            {
                player.sendMessage(new TextComponent("§d[Groupe] §e" + target.getDisplayName() + " §cest déjà dans votre groupe !"));
            }
        }
    }

    public static void sendRequest(ProxiedPlayer sender, ProxiedPlayer target){
        Optional<Account> account = RcbProxy.getInstance().getAccount(sender);
        Optional<Account> account2 = RcbProxy.getInstance().getAccount(target);
        if(account.isPresent() && account2.isPresent()){
            if(account.get().getDataParty().isInParty() && account2.get().getDataParty().isInParty()){
                if(!account.get().getDataParty().getParty().equals(account2.get().getDataParty().getParty())){
                    sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + account2.get().getDatarank().getRank().getPrefix() + target.getName()));
                    target.sendMessage(new TextComponent("§e-----------------------------"));
                    target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + account.get().getDatarank().getRank().getPrefix() + sender.getName()));
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
                    h.put(target, account.get().getDataParty().getParty());
                    datarequest.put(sender, h);
                }
                else 
                {
                    sender.sendMessage(new TextComponent("§d[Groupe] §cVous etes déjà dans le meme groupe !"));
                    return;
                }
            }
            else if(!account.get().getDataParty().isInParty()){
                Party party = new Party(5);
                party.addPlayer(sender);
                party.setCaptain(sender);
                sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + account2.get().getDatarank().getRank().getPrefix() + target.getName()));
                target.sendMessage(new TextComponent("§e-----------------------------"));
                target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + account.get().getDatarank().getRank().getPrefix() + sender.getName()));
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
                h.put(target, account.get().getDataParty().getParty());
                datarequest.put(sender, h);
            }
            else
            {

                sender.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aenvoyé §6une invitation de groupe à " + account2.get().getDatarank().getRank().getPrefix() + target.getName()));
                target.sendMessage(new TextComponent("§e-----------------------------"));
                target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §arecu §6une invitation de groupe de " + account.get().getDatarank().getRank().getPrefix() + sender.getName()));
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
                h.put(target, account.get().getDataParty().getParty());
                datarequest.put(sender, h);
            }
        }
    }

    public static void denyRequest(ProxiedPlayer sender, ProxiedPlayer target, Party party){
        Optional<Account> account = RcbProxy.getInstance().getAccount(sender);
        if(account.isPresent()){
            if(datarequest.containsKey(sender)){
                HashMap<ProxiedPlayer, Party> value = datarequest.get(sender);
                if(value.containsKey(target)){
                    if(value.get(target).equals(party)){
                        datarequest.get(sender).remove(target);
                        if(RcbProxy.getInstance().getAccount(sender).isPresent()){
                            target.sendMessage(new TextComponent("§d[Groupe] §cVous avez décliné l'invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).get().getDatarank().getRank().getPrefix() + sender.getName()));
                        }
                        else
                        {
                            target.sendMessage(new TextComponent("§d[Groupe] §cVous avez décliné l'invitation de groupe de " + sender.getDisplayName()));
                        }
                    }
                }
            }
            else
            {
                target.sendMessage(new TextComponent("§d[Groupe] §cRequête introuvable !"));
            }
        }
    }

    public static void acceptRequest(ProxiedPlayer sender, ProxiedPlayer target, Party party){
        Optional<Account> account = RcbProxy.getInstance().getAccount(sender);
        if(account.isPresent()){
            if(datarequest.containsKey(sender)){
                HashMap<ProxiedPlayer, Party> value = datarequest.get(sender);
                if(value.containsKey(target)){
                    if(value.get(target).equals(party)){
                        datarequest.get(sender).remove(target);
                        if(RcbProxy.getInstance().getAccount(sender).isPresent()){
                            addMember(sender, target);
                            target.sendMessage(new TextComponent("§d[Groupe] §6Vous avez §aaccepté §6l'invitation de groupe de " + RcbProxy.getInstance().getAccount(sender).get().getDatarank().getRank().getPrefix() + sender.getDisplayName()));
                        }
                    }
                }
            }
            else
            {
                target.sendMessage(new TextComponent("§d[Groupe] §cRequête introuvable !"));
            }
        }
    }

    public static HashMap<ProxiedPlayer, HashMap<ProxiedPlayer, Party>> getDatarequest() {
        return datarequest;
    }
}
