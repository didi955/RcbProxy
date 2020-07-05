package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.account.Account;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Btp extends Command {

    private static final List<String> cmds = Arrays.asList("btp", "bungeeteleport");

    public Btp(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Seul un joueur peut effectuer cette commande !"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        Optional<Account> account = RcbProxy.getInstance().getAccount(player);
        if (!sender.hasPermission("rcbproxy.tp")) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Vous n'avez pas la permission de faire ceci !"));
            return;
        }
        if (args.length < 1) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Erreur: /btp <player> [<player>]"));

            return;
        }
        if (args.length == 1) {

            ProxiedPlayer from = (ProxiedPlayer) sender;
            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[0]);


            if (args[0] != null && to == null) {
                from.sendMessage(new TextComponent(ChatColor.RED + "Le joueur n'est pas en-ligne !"));

                return;
            }
            teleport(from, to);
            from.sendMessage(new TextComponent(ChatColor.GREEN + "Vous avez été téleporté vers " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + to.getName()));

            return;
        }

        if (args.length == 2) {

            ProxiedPlayer from = ProxyServer.getInstance().getPlayer(args[0]);

            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[1]);


            if (from == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + args[0] + " n'est pas en-ligne !"));

                return;
            }
            if (to == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + args[1] + " n'est pas en-ligne !"));

                return;
            }
            teleport(from, to);

            sender.sendMessage(new TextComponent(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + from.getName() + ChatColor.RESET + "" + ChatColor.GREEN + " a été téleporté vers " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + to.getName() + "."));
        }

    }

    public static void teleport(ProxiedPlayer from, ProxiedPlayer to) {
        if(from == to){
            return;
        }
        if (from.getServer().getInfo() != to.getServer().getInfo()) {
            from.connect(to.getServer().getInfo());
        }

        ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(RcbProxy.getInstance(), () -> BungeeSend.teleport(from, to), 1L, TimeUnit.SECONDS);
    }

    public static List<String> getCmds() {
        return cmds;
    }
}