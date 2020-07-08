package fr.rushcubeland.rcbproxy.bukkit.friends;

import fr.rushcubeland.rcbproxy.bukkit.network.ServerUnit;
import fr.rushcubeland.rcbproxy.bukkit.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsGUI {

    private static final HashMap<Player, ArrayList<String>> dataFriends = new HashMap<>();

    public static void openFriendsGUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "§d[Amis]");
        if(dataFriends.containsKey(player)){
            ArrayList<String> friends = dataFriends.get(player);
            friends = sortFriends(friends);
            int a = 0;
            for(String friend : friends){
                Player target = Bukkit.getPlayer(friend);
                ItemStack head;
                if(target == null){
                    head = new ItemBuilder(Material.PLAYER_HEAD).setName("§b" + friend).setLore("§c ", "§fStatut: §cHors-ligne").setSkullOwner(Bukkit.getOfflinePlayer(friend)).toItemStack();
                }
                else
                {
                    if(ServerUnit.getByPort(target.getServer().getPort()).isPresent()){
                        String serverName = ServerUnit.getByPort(target.getServer().getPort()).get().getName();
                        head = new ItemBuilder(Material.PLAYER_HEAD).setName(friend).setLore("§c ", "§fStatut: §aEn-ligne", "§fServeur: §6" + serverName).setSkullOwner(Bukkit.getPlayer(friend)).toItemStack();
                    }
                    else
                    {
                        head = new ItemBuilder(Material.PLAYER_HEAD).setName(friend).setLore("§c ", "§fStatut: §aEn-ligne", "§fServeur: §6Null").setSkullOwner(Bukkit.getPlayer(friend)).toItemStack();
                    }
                }
                inv.setItem(a, head);
                a = a+1;
            }
        }
        inv.setItem(53, new ItemBuilder(Material.ACACIA_DOOR).setName("§cQuitter").toItemStack());
        player.openInventory(inv);
    }

    private static ArrayList<String> sortFriends(ArrayList<String> arrayList){
        int count = 0;
        if(arrayList.size() <= 1){
            return arrayList;
        }
        for(String friend : arrayList){
            String bfriend = arrayList.get(count+1);
            Player fp = Bukkit.getPlayer(friend);
            Player fpb = Bukkit.getPlayer(bfriend);
            if(fpb != null && fp == null){
                arrayList.remove(friend);
                arrayList.add(friend);
            }
            else if(fpb == null && fp != null){
                continue;
            }
            else if(fp != null){
                continue;
            }
            count = count+1;
        }
        return arrayList;
    }

    public static HashMap<Player, ArrayList<String>> getDataFriends() {
        return dataFriends;
    }

}
