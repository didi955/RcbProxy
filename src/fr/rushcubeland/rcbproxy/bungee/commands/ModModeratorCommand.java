package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.BungeeSend;
import fr.rushcubeland.rcbproxy.bungee.mod.ModModerator;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class ModModeratorCommand extends Command {


    public ModModeratorCommand() {
        super("mod");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if(!player.hasPermission("rcbproxy.mod")){
                player.sendMessage(new TextComponent("§cVous n'avez pas la permission de faire ceci !"));
                return;
            }
            if(ModModerator.isInModData(player.getUniqueId().toString())){
                ModModerator.removeMod(player.getUniqueId().toString());
                BungeeSend.sendModModeratorDataRemove(player.getUniqueId().toString());
                player.sendMessage(new TextComponent("§6Vous avez §cquitté §6le mode §cModérateur"));
                return;
            }
            ModModerator.addMod(player.getUniqueId().toString());
            BungeeSend.sendModModeratorDataAdd(player.getUniqueId().toString());
            player.sendMessage(new TextComponent("§aVous êtes désormais en mode §6Modérateur"));
        }
    }

}
