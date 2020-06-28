package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Optional;

public class WhoisCommand extends Command {

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
                infos(player, target);
            }
        }
    }

    public void infos(ProxiedPlayer sender, ProxiedPlayer target) {
        Optional<Account> account = RcbProxy.getInstance().getAccount(target);
        account.ifPresent(value -> sender.sendMessage(new TextComponent("§6[Whois] " + value.getDatarank().getRank().getPrefix() + target.getName() + " :")));
        String ip = target.getAddress().getHostString();
        int port = target.getAddress().getPort();
        sender.sendMessage(new TextComponent("§fConnexion: §7" + ip + ":" + port));
        sender.sendMessage(new TextComponent("§fServeur: §c" + target.getServer().getInfo().getName()));
        sender.sendMessage(new TextComponent("§fPing: " + target.getPing()));
    }
}
