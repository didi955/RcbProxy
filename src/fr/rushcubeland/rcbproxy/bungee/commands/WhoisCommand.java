package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.exceptions.AccountNotFoundException;
import fr.rushcubeland.rcbproxy.bungee.mod.ModModerator;
import fr.rushcubeland.rcbproxy.bungee.provider.AccountProvider;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WhoisCommand extends Command {

    private static String cmd = "whois";

    public WhoisCommand() {
        super("whois");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission("rcbproxy.whois")){
                player.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
                return;
            }
            if(args.length < 1){
                infos(player, player);
            }
            if(args.length == 1){
                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                if(target != null){
                    infos(player, target);
                }
            }
        }
    }

    public void infos(ProxiedPlayer sender, ProxiedPlayer target) {
        BungeeCord.getInstance().getScheduler().runAsync(RcbProxy.getInstance(), () -> {

            try {

                final AccountProvider accountProvider = new AccountProvider(target);
                final Account account = accountProvider.getAccount();

                if(ModModerator.isInModData(target.getUniqueId().toString())){
                    sender.sendMessage(new TextComponent("§6[Whois] " + account.getRank().getPrefix() + target.getName() + " §6(MOD) §f:"));
                }
                else
                {
                    sender.sendMessage(new TextComponent("§6[Whois] " + account.getRank().getPrefix() + target.getName() + " :"));
                }

            } catch (AccountNotFoundException exception) {
                System.err.println(exception.getMessage());
            }
        });
        String ip = target.getAddress().getHostString();
        int port = target.getAddress().getPort();
        sender.sendMessage(new TextComponent("§fConnexion: §7" + ip + ":" + port));
        sender.sendMessage(new TextComponent("§fServeur: §c" + target.getServer().getInfo().getName()));
        sender.sendMessage(new TextComponent("§fPing: §7" + target.getPing() + "ms"));
        sender.sendMessage(new TextComponent("§fVersion: §e" + getVersionStringPlayer(target)));
    }

    private String getVersionStringPlayer(ProxiedPlayer player){
        int version = player.getPendingConnection().getVersion();
        String versionstr = "Non officielle";
        if(version == 573){
            versionstr = "1.15";
        }
        if(version == 575){
            versionstr = "1.15.1";
        }
        if(version == 578){
            versionstr = "1.15.2";
        }
        if(version == 735){
            versionstr = "1.16";
        }
        if(version == 736){
            versionstr = "1.16.1";
        }
        return versionstr;
    }

    public static String getCmd() {
        return cmd;
    }
}
