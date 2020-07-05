package fr.rushcubeland.rcbproxy.bungee.report;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class Report {

    private static final HashMap<ProxiedPlayer, Boolean> reportToogleData = new HashMap<>();

    public static void report(ProxiedPlayer player, ProxiedPlayer target, String raison) {
        for (ProxiedPlayer mods : ProxyServer.getInstance().getPlayers()) {
            if (mods.hasPermission("rcbproxy.report")) {
                if(!reportToogleData.containsKey(mods)){
                    mods.sendMessage(new TextComponent("§d[Report] §e---------"));
                    TextComponent l2 = new TextComponent("§fJoueur signalé: §6");
                    TextComponent l3 = new TextComponent("§7- par §d");
                    TextComponent targetn = new TextComponent(target.getName());
                    TextComponent playern = new TextComponent(player.getName());
                    targetn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + target.getName()));
                    targetn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("/btp "+ target.getName())));
                    playern.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + player.getName()));
                    playern.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("/btp "+ player.getName())));
                    mods.sendMessage((new ComponentBuilder(l2)).append(targetn).create());
                    mods.sendMessage((new ComponentBuilder(l3)).append(playern).create());
                    mods.sendMessage(new TextComponent("§fRaison: §b" + raison + "§b"));
                    mods.sendMessage(new TextComponent("§e----------------"));
                }
                else if(reportToogleData.get(mods)){
                    mods.sendMessage(new TextComponent("§d[Report] §e---------"));
                    TextComponent l2 = new TextComponent("§fJoueur signalé: §6");
                    TextComponent l3 = new TextComponent("§7- par §d");
                    TextComponent targetn = new TextComponent(target.getName());
                    TextComponent playern = new TextComponent(player.getName());
                    targetn.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + target.getName()));
                    targetn.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("/btp "+ target.getName())));
                    playern.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/btp " + player.getName()));
                    playern.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("/btp "+ player.getName())));
                    mods.sendMessage((new ComponentBuilder(l2)).append(targetn).create());
                    mods.sendMessage((new ComponentBuilder(l3)).append(playern).create());
                    mods.sendMessage(new TextComponent("§fRaison: §b" + raison + "§b"));
                    mods.sendMessage(new TextComponent("§e----------------"));
                }
            }
        }
    }

    public static HashMap<ProxiedPlayer, Boolean> getReportToogleData() {
        return reportToogleData;
    }
}
