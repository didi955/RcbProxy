package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.parties.Parties;
import fr.rushcubeland.rcbproxy.bungee.parties.Party;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PartyCommand extends Command {


    private static final List<String> cmds = Arrays.asList("group", "party", "p", "g");

    public PartyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            Optional<Account> account = RcbProxy.getInstance().getAccount(player);

            if(args.length == 0){
                infos(player);
                return;
            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("list")){
                    if(account.isPresent()){
                        if(account.get().getDataParty().isInParty()){
                            Party party = account.get().getDataParty().getParty();
                            player.sendMessage(new TextComponent("§e---------§d[Groupe]§e---------"));
                            for(ProxiedPlayer pls : party.getPlayers()){
                                if(party.getCaptain() == pls){
                                    player.sendMessage(new TextComponent("§b" + pls.getDisplayName() + " §7<> " + pls.getServer().getInfo().getName() + " §7[Capitaine]"));
                                }
                                else
                                {
                                    player.sendMessage(new TextComponent("§b" + pls.getDisplayName() + " §7<> " + pls.getServer().getInfo().getName()));
                                }
                            }
                            player.sendMessage(new TextComponent("§e-------------------------"));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§d[Groupe] §cVous n'êtes pas dans un groupe !"));
                            return;
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                        player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                        return;
                    }
                }
                if(args[0].equalsIgnoreCase("leave") || args[0].equalsIgnoreCase("quit")){
                    if(account.get().getDataParty().isInParty()){
                        Parties.leave(player);
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                        player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                        return;
                    }
                }
                if(args[0].equalsIgnoreCase("disband")){
                    if(account.isPresent()){
                        Party party = account.get().getDataParty().getParty();
                        if(party.getCaptain() == player){
                            for(ProxiedPlayer pls : party.getPlayers()){
                                Optional<Account> a = RcbProxy.getInstance().getAccount(pls);
                                pls.sendMessage(new TextComponent("§d[Groupe] §cVotre groupe a été dissout par §b" + player.getDisplayName() + " §7[Capitaine]"));
                                if(a.isPresent()){
                                    a.get().getDataParty().setParty(null);
                                    party.disbandParty();
                                }
                            }
                            player.sendMessage(new TextComponent("§d[Groupe] §cVous avez dissous le groupe !"));
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§d[Groupe] §cVous ne pouvez pas dissoudre le groupe en"));
                            player.sendMessage(new TextComponent("§cn'etant pas capitaine !"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                        player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                        return;
                    }

                }
                if(args[0].equalsIgnoreCase("lead") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny")){
                    player.sendMessage(new TextComponent("§d[Groupe] §cVeuillez spécifier un joueur !"));
                    return;
                }
            }
            if(args.length == 2){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
                Optional<Account> account2 = RcbProxy.getInstance().getAccount(target);
                if(target != null){
                    if(args[0].equalsIgnoreCase("add")){
                        if(account.isPresent()){
                            if(account.get().getDataParty().isInParty()){
                                Parties.sendRequest(player, target);
                            }
                            else
                            {
                                Party party = new Party(5);
                                party.addPlayer(player);
                                Parties.sendRequest(player, target);
                                return;
                            }
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                            player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                            return;
                        }
                    }
                    if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("dl")){
                        if(account.isPresent()){
                            if(account.get().getDataParty().isInParty()){
                                if(account.get().getDataParty().getParty().getCaptain() == player){
                                    Parties.removeMember(player, target);
                                }
                                else
                                {
                                    player.sendMessage(new TextComponent("§d[Groupe] §cSeul le capitaine du groupe peut exclure des membres !"));
                                    return;
                                }
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cVous n'êtes pas dans un groupe !"));
                                return;
                            }
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                            player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                            return;
                        }
                    }
                    if(args[0].equalsIgnoreCase("accept")){
                        if(account2.isPresent()){
                            if(account2.get().getDataParty().isInParty()){
                                Parties.acceptRequest(target, player, account2.get().getDataParty().getParty());
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cCe joueur n'est plus dans une partie !"));
                            }
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                            player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                            return;
                        }
                    }
                    if(args[0].equalsIgnoreCase("deny")){
                        if(account2.isPresent()){
                            if(account2.get().getDataParty().isInParty()){
                                Parties.denyRequest(target, player, account2.get().getDataParty().getParty());
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cCe joueur n'est plus dans une partie !"));
                            }
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                            player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                            return;
                        }

                    }
                    if(args[0].equalsIgnoreCase("lead")){
                        if(account.isPresent()){
                            if(account.get().getDataParty().isInParty()){
                                Party party = account.get().getDataParty().getParty();
                                if(account2.isPresent()){
                                    if(party.equals(account2.get().getDataParty().getParty())){
                                        if(party.getCaptain().equals(player)){
                                            party.setCaptain(target);
                                            for(ProxiedPlayer pls : party.getPlayers()){
                                                pls.sendMessage(new TextComponent("§d[Groupe] §b" + player.getDisplayName() + " §aa transmis le lead à §e" + target.getDisplayName()));
                                            }
                                        }
                                        else
                                        {
                                            player.sendMessage(new TextComponent("§d[Groupe] §cSeul le capitaine du groupe peut transmettre le lead à un membre !"));
                                            return;
                                        }
                                    }
                                    else
                                    {
                                        player.sendMessage(new TextComponent("§d[Groupe] §cVous n'etes pas dans le meme groupe !"));
                                        return;
                                    }
                                }
                                else
                                {
                                    player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                                    player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                                    return;
                                }
                            }
                            else
                            {
                                player.sendMessage(new TextComponent("§d[Groupe] §cVous n'etes pas dans un groupe !"));
                                return;
                            }
                        }
                        else
                        {
                            player.sendMessage(new TextComponent("§cVotre compte est introuvable, veuillez vous reconnecter."));
                            player.sendMessage(new TextComponent("§cSi le problème persite, veuillez contacter un administrateur."));
                            return;
                        }
                    }
                }
            }
        }
    }

    private void infos(ProxiedPlayer player){
        player.sendMessage(new TextComponent("§e---------§d[Groupe]§e---------"));
        player.sendMessage(new TextComponent("§6/group list - §eAfficher la liste des joueurs de votre groupe"));
        player.sendMessage(new TextComponent("§6/group add <joueur> - §eEnvoyer une invitation de groupe"));
        player.sendMessage(new TextComponent("§6/group remove <joueur> - §eExclure un joueur de votre groupe"));
        player.sendMessage(new TextComponent("§6/group leave - §eQuitter votre groupe"));
        player.sendMessage(new TextComponent("§6/group lead <joueur> - §eDéfinir le role de capitaine"));
        player.sendMessage(new TextComponent("§e à un joueur de votre groupe"));
        player.sendMessage(new TextComponent("§6/group accept <joueur> - §eAccepter l'invitation d'un joueur"));
        player.sendMessage(new TextComponent("§epour rejoindre son groupe"));
        player.sendMessage(new TextComponent("§6/group deny <joueur> - §eRefuser l'invitation d'un"));
        player.sendMessage(new TextComponent("§ejoueur"));
        player.sendMessage(new TextComponent("§6/group disband - §eDissoudre votre groupe"));
        player.sendMessage(new TextComponent("§e-----------------------"));
    }

    public static List<String> getCmds() {
        return cmds;
    }
}
