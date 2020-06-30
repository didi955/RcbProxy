package fr.rushcubeland.rcbproxy.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.TimeUnit;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class MuteCommand extends Command {

    public MuteCommand() {
        super("mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer && !sender.hasPermission("rcbproxy.mute")){
            sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            return;
        }
        if (args.length < 3) {
            helpMessage(sender);
            return;
        }
        String targetName = args[0];
        UUID targetUUID;

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);
        if(target == null){
            String uuids = UUIDFetcher.getUUIDFromName(targetName);
            if(uuids == null){
                sender.sendMessage(new TextComponent("§cCe joueur n'existe pas !"));
                return;
            }
            else {
                targetUUID = UUID.fromString(uuids);
            }
        }
        else
        {
            targetUUID = target.getUniqueId();
        }

        if (RcbProxy.getInstance().getMuteManager().isMuted(targetUUID)) {
            sender.sendMessage(new TextComponent("§cCe joueur est déjà mute !"));
            return;
        }

        String reason = "";
        for (int i = 2; i < args.length; i++) {
            reason = reason + args[i] + " ";
        }

        if (args[1].equalsIgnoreCase("perm")) {
            RcbProxy.getInstance().getMuteManager().mute(targetUUID, -1L, reason);
            sender.sendMessage(new TextComponent("§aVous avez mute §6" + targetName + " §c(Permanent) §apour : §e" + reason));
            return;
        }
        if (!args[1].contains(":")) {
            helpMessage(sender);
            return;
        }

        int duration = 0;
        try {
            duration = Integer.parseInt(args[1].split(":")[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponent("§cLa valeur 'durée' doit être un nombre !"));

            return;
        }
        if (!TimeUnit.existFromShortcut(args[1].split(":")[1])) {
            sender.sendMessage(new TextComponent("§cCette unité de temps n'existe pas !"));
            for (TimeUnit units : TimeUnit.values()) {
                sender.sendMessage(new TextComponent("§b" + units.getName() + " §f: §e" + units.getShortcut()));
            }
            return;
        }
        TimeUnit unit = TimeUnit.getFromShortcut(args[1].split(":")[1]);
        long muteTime = unit.getToSecond() * duration;

        RcbProxy.getInstance().getMuteManager().mute(targetUUID, muteTime, reason);
        sender.sendMessage(new TextComponent("§aVous avez mute §6" + targetName + " §b(" + duration + " " + unit.getName() + ") §apour : §e" + reason));

    }
    public void helpMessage(CommandSender sender) {
        sender.sendMessage(new TextComponent("§c/mute <joueur> perm <raison>"));
        sender.sendMessage(new TextComponent("§c/mute <joueur> <durée>:<unité> <raison>"));
    }

}
