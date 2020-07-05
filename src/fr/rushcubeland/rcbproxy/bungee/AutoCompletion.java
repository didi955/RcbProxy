package fr.rushcubeland.rcbproxy.bungee;

import fr.rushcubeland.rcbproxy.bungee.account.Account;
import fr.rushcubeland.rcbproxy.bungee.commands.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AutoCompletion implements Listener {


    @EventHandler(priority = 32)
    public void onTab(TabCompleteEvent e) {
        String[] args = e.getCursor().toLowerCase().split(" ");

        if (args.length >= 1){
            if (args[0].startsWith("/")){

                if (Btp.getCmds().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                    if (args.length == 2) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                        addSuggestions(e, args);
                    }
                }
                if(WhoisCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(MuteCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(BanCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(UnmuteCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(PunishGUICommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(KickCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if (args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(ReportCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if(args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(MPCommand.getCmd().contains(args[0].replaceAll("/", "")) && e
                        .getCursor().contains(" ")) {

                    e.getSuggestions().clear();

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if(args.length == 1) {
                        for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                            e.getSuggestions().add(all.getName());
                        }
                        return;
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                }
                if(FriendCommand.getCmds().contains(args[0].replaceAll("/", "")) && e
                .getCursor().contains(" ")){

                    ProxiedPlayer p = (ProxiedPlayer)e.getSender();

                    if(args.length == 1){
                        e.getSuggestions().add("list");
                        e.getSuggestions().add("remove");
                        e.getSuggestions().add("add");
                        e.getSuggestions().add("accept");
                        e.getSuggestions().add("deny");
                    }
                    if (args.length == 2 && getSpace(e.getCursor()) == 1) {
                        addSuggestions(e, args);
                        return;
                    }
                    if(args.length == 2){
                        if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("rm") || args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete")){
                            Optional<Account> account = RcbProxy.getInstance().getAccount(p);
                            if(account.isPresent()){
                                for(String friend : account.get().getDataFriends().getFriends()){
                                    e.getSuggestions().add(friend);
                                }
                            }
                        }
                        if(args[1].equalsIgnoreCase("add")){
                            for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
                                e.getSuggestions().add(all.getName());
                            }
                        }
                    }
                    if (args.length == 3 && getSpace(e.getCursor()) == 2) {
                        addSuggestions(e, args);
                    }
                }
            }
        }
    }

    private void addSuggestions(TabCompleteEvent e, String[] args) {
        String check = args[args.length - 1];
        List<String> argFriend = Arrays.asList("list", "remove", "deny", "accept", "add");

        for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
            if (all.getName().toLowerCase().startsWith(check)) {
                e.getSuggestions().add(all.getName());
            }
        }
        for(String s : argFriend){
            if(s.startsWith(check)){
                e.getSuggestions().add(s);
            }
        }
    }


    public static int getSpace(String s) {
        int space = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                space++;
            }
        }
        return space;
    }


}
