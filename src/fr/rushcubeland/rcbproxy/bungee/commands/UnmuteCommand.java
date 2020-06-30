package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        super("unmute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer && !sender.hasPermission("rcbproxy.unmute")){
            sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c/unmute <joueur>"));
            return;
        }
        String targetName = args[0];

        String uuids = UUIDFetcher.getUUIDFromName(targetName);
        if(uuids == null){
            sender.sendMessage(new TextComponent("§cCe joueur n'existe pas !"));
            return;
        }
        UUID targetUUID = UUID.fromString(uuids);

        if (!(RcbProxy.getInstance().getMuteManager().isMuted(targetUUID))) {
            sender.sendMessage(new TextComponent("§cCe joueur n'est pas mute !"));
            return;
        }
        RcbProxy.getInstance().getMuteManager().unmute(targetUUID);
        sender.sendMessage(new TextComponent("§aVous avez démute §6" + targetName));
    }
}

