package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnbanCommand extends Command {

    public UnbanCommand() {
        super("unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer && sender.hasPermission("rcbproxy.unban")){
            sender.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
            return;
        }
        if (args.length != 1) {
            sender.sendMessage(new TextComponent("§c/unban <joueur>"));
            return;
        }
        String targetName = args[0];

        UUID targetUUID = UUID.fromString(UUIDFetcher.getUUIFromName(targetName));

        if (!(RcbProxy.getInstance().getBanManager().isBanned(targetUUID))) {
            sender.sendMessage(new TextComponent("§cCe joueur n'est pas banni !"));
            return;
        }
        RcbProxy.getInstance().getBanManager().unban(targetUUID);
        sender.sendMessage(new TextComponent("§aVous avez débanni §6" + targetName));
    }
}
