package fr.rushcubeland.rcbproxy.bungee.commands;

import fr.rushcubeland.rcbproxy.bungee.RcbProxy;
import fr.rushcubeland.rcbproxy.bungee.slots.Slots;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class SlotsCommand extends Command {

    public SlotsCommand(String name) {
        super(name);
    }

    private static final List<String> cmds = Arrays.asList("setslot", "setslots", "slot", "slots");

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("§c/setslots <slots>").create());
            return;
        }

        try {
            int slots = Integer.parseInt(args[0]);

            Slots.changeSlots(slots);
            Slots.updateSlotsConfig(slots);

            sender.sendMessage(new TextComponent("§aLe nombre de slots du proxy a été  mis a jour sur: §c" + args[0]));
        } catch (NumberFormatException e) {
            sender.sendMessage(new TextComponent("§cVeuillez spécifier un nombre !"));
            sender.sendMessage(new TextComponent("§c/setslots <nombre>"));
        } catch (ReflectiveOperationException e) {
            sender.sendMessage(new TextComponent("§cErreur en executant la commande !"));
            sender.sendMessage(new TextComponent("§c/setslots <nombre>"));

            ProxyServer.getInstance().getLogger().log(Level.SEVERE, "An error occurred while updating max players", e);
        }
    }

    public static List<String> getCmds() {
        return cmds;
    }
}
