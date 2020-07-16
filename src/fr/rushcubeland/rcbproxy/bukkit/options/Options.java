package fr.rushcubeland.rcbproxy.bukkit.options;

import fr.rushcubeland.rcbproxy.bukkit.BukkitSend;
import fr.rushcubeland.rcbproxy.bukkit.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Options {

    private static final HashMap<Player, String> dataStateChat = new HashMap<>();
    private static final HashMap<Player, String> dataStatePartyInvite = new HashMap<>();
    private static final HashMap<Player, String> dataStateFriendRequests = new HashMap<>();
    private static final HashMap<Player, String> dataStateFriendsStatutNotif = new HashMap<>();
    private static final HashMap<Player, String> dataStateMP = new HashMap<>();


    public static boolean hasChatActivated(Player player){
        if(dataStateChat.containsKey(player)){
            String option = dataStateChat.get(player);
            return option.equalsIgnoreCase("OPEN");
        }
        return false;
    }

    public static String getStateOfPartyInvite(Player player){
        if(dataStatePartyInvite.containsKey(player)){
            return dataStatePartyInvite.get(player);
        }
        return null;
    }

    public static String getStateOfFriendRequests(Player player){
        if(dataStateFriendRequests.containsKey(player)){
            return dataStateFriendRequests.get(player);
        }
        return null;
    }

    public static String getStateOfFriendsStatutNotif(Player player){
        if(dataStateFriendsStatutNotif.containsKey(player)){
            return dataStateFriendsStatutNotif.get(player);
        }
        return null;
    }

    public static void openOptionsInv(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "§cPréférences");

        ItemStack chat;
        ItemStack state;
        if(dataStateChat.containsKey(player)){
            if(dataStateChat.get(player).equalsIgnoreCase("OPEN")){
                chat = new ItemBuilder(Material.PAPER).setName("§6Afficher le chat").setLore("§fActiver ou désactiver le chat", "§c ", "§bActuellement: §aActivé").toItemStack();
                state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
            }
            else
            {
                chat = new ItemBuilder(Material.PAPER).setName("§6Afficher le chat").setLore("§fActiver ou désactiver le chat", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
            }
            inv.setItem(0, chat);
            inv.setItem(1, state);
        }


        ItemStack friendRequests;
        ItemStack state2;
        if(dataStateFriendRequests.containsKey(player)){
            if(dataStateFriendRequests.get(player).equalsIgnoreCase("OPEN")){
                friendRequests = new ItemBuilder(Material.PLAYER_HEAD).setName("§6Recevoir des requetes d'amis").setLore("§fActiver ou désactiver le fait de recevoir des requetes d'amis", "§c ", "§bActuellement: §aActivé").toItemStack();
                state2 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
            }
            else
            {
                friendRequests = new ItemBuilder(Material.PLAYER_HEAD).setName("§6Recevoir des requetes d'amis").setLore("§fActiver ou désactiver le fait de recevoir des requetes d'amis", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                state2 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
            }
            inv.setItem(3, friendRequests);
            inv.setItem(4, state2);
        }

        ItemStack mp;
        ItemStack state3;
        if(dataStateMP.containsKey(player)){
            if(dataStateMP.get(player).equalsIgnoreCase("OPEN")){
                mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §aActivé").toItemStack();
                state3 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                inv.setItem(18, mp);
                inv.setItem(19, state3);
            }
            else if(dataStateMP.get(player).equalsIgnoreCase("ONLY_FRIENDS"))
            {
                mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §eAmis uniquement").toItemStack();
                state3 = new ItemBuilder(Material.CYAN_DYE).setName("§bActuellement: §eAmis uniquement").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                inv.setItem(18, mp);
                inv.setItem(19, state3);
            }
            else
            {
                mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                state3 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                inv.setItem(18, mp);
                inv.setItem(19, state3);
            }
        }

        ItemStack notif;
        ItemStack state4;
        if(dataStateFriendsStatutNotif.containsKey(player)){
            if(dataStateFriendsStatutNotif.get(player).equalsIgnoreCase("OPEN")){
                notif = new ItemBuilder(Material.NETHER_STAR).setName("§6Notifications de co/deco des amis").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §aActivé").toItemStack();
                state4 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
            }
            else
            {
                notif = new ItemBuilder(Material.NETHER_STAR).setName("§6Notifications de co/deco des amis").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                state4 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
            }
            inv.setItem(21, notif);
            inv.setItem(22, state4);
        }

        ItemStack party;
        ItemStack state5;
        if(dataStatePartyInvite.containsKey(player)){
            if(dataStatePartyInvite.get(player).equalsIgnoreCase("OPEN")){
                party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §aActivé").toItemStack();
                state5 = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
            }
            else if(dataStateMP.get(player).equalsIgnoreCase("ONLY_FRIENDS"))
            {
                party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §eAmis uniquement").toItemStack();
                state5 = new ItemBuilder(Material.CYAN_DYE).setName("§bActuellement: §eAmis uniquement").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
            }
            else
            {
                party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                state5 = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
            }
            inv.setItem(36, party);
            inv.setItem(37, state5);
        }

        ItemStack close = new ItemBuilder(Material.ACACIA_DOOR).setName("§cFermer").toItemStack();
        inv.setItem(53, close);

        player.openInventory(inv);
    }

    public static void updateDataStateChat(Player player, String state){
        dataStateChat.put(player, state);
    }

    public static void updateDataStatePartyInvite(Player player, String state){
        dataStatePartyInvite.put(player, state);
    }

    public static void updateDataStateFriendRequests(Player player, String state){
        dataStateFriendRequests.put(player, state);
    }

    public static void updateDataStateFriendsStatutNotif(Player player, String state){
        dataStateFriendsStatutNotif.put(player, state);
    }

    public static void updateDataStateMP(Player player, String state){
        dataStateMP.put(player, state);
    }

    public static void updateDataStateChatProxy(Player player, String state){
        BukkitSend.StateChatToProxy(player, state);
    }

    public static void updateDataStateMPProxy(Player player, String state){
        BukkitSend.StateMPToProxy(player, state);
    }

    public static void updateDataStatePartyInviteProxy(Player player, String state){
        BukkitSend.StatePartyInviteToProxy(player, state);
    }

    public static void updateDataStateFriendRequestsProxy(Player player, String state){
        BukkitSend.StateFriendRequestsToProxy(player, state);
    }

    public static void updateDataStateFriendsStatutNotifProxy(Player player, String state){
        BukkitSend.StateFriendsStatutNotifToProxy(player, state);
    }

    public static HashMap<Player, String> getDataStateChat() {
        return dataStateChat;
    }

    public static HashMap<Player, String> getDataStatePartyInvite() {
        return dataStatePartyInvite;
    }

    public static HashMap<Player, String> getDataStateFriendRequests() {
        return dataStateFriendRequests;
    }

    public static HashMap<Player, String> getDataFriendsStatutNotif() {
        return dataStateFriendsStatutNotif;
    }

    public static HashMap<Player, String> getDataMP() {
        return dataStateMP;
    }
}
