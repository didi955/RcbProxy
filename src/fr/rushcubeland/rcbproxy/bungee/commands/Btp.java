package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Btp extends Command {

    private static List<String> cmds = Arrays.asList(new String[] { "btp", "bungeeteleport" });

    public Btp(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Command only for player !"));

            return;
        }
        if (!sender.hasPermission("bungeeteleport.tp")) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You don't have permission to execute this command !"));

            return;
        }
        if (args.length < 1) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Error: /btp <player> [<player>]"));

            return;
        }
        if (args.length == 1) {

            ProxiedPlayer from = (ProxiedPlayer)sender;
            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[0]);


            if (args[0] != null && to == null) {
                from.sendMessage(new TextComponent(ChatColor.RED + "This player is not online !"));

                return;
            }
            teleport(from, to);
            from.sendMessage(new TextComponent(ChatColor.GREEN + "You have been teleported to " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + to.getName()));

            return;
        }

        if (args.length == 2) {

            if (!sender.hasPermission("bungeeteleport.tp.others")) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "You don't have permission to execute this command !"));

                return;
            }
            ProxiedPlayer from = ProxyServer.getInstance().getPlayer(args[0]);

            ProxiedPlayer to = ProxyServer.getInstance().getPlayer(args[1]);


            if (from == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + args[0] + " is not online !"));

                return;
            }
            if (to == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + args[1] + " is not online !"));

                return;
            }
            teleport(from, to);

            sender.sendMessage(new TextComponent(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + from.getName() + ChatColor.RESET + "" + ChatColor.GREEN + " has been teleported to " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + to.getName() + "."));
        }

    }

    public static void teleport(ProxiedPlayer from, ProxiedPlayer to) {
        if (from.getServer().getInfo() != to.getServer().getInfo()) {
            from.connect(to.getServer().getInfo());
        }

        ScheduledTask schedule = ProxyServer.getInstance().getScheduler().schedule(RcbProxy.getInstance(), () -> BungeeSend.teleport(from, to), 1L, TimeUnit.SECONDS);
    }

    public static List<String> getCmds() {
        return cmds;
    }
}