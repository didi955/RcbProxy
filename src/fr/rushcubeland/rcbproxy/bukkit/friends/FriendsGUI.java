package fr.rushcubeland.rcbproxy.bukkit.friends;

import fr.rushcubeland.rcbproxy.bukkit.RcbProxy;
import fr.rushcubeland.rcbproxy.bukkit.network.ServerUnit;
import fr.rushcubeland.rcbproxy.bukkit.utils.ItemBuilder;
import fr.rushcubeland.rcbproxy.bukkit.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class FriendsGUI {

    public static void openFriendsGUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "§d[Amis]");

        RcbProxy.getInstance().getAccountFriendsCallback(player, aFriends -> {

            List<UUID> sortedFriends = sortFriends(aFriends.getFriends());

            int a = 0;
            for(UUID friend : sortedFriends){
                Player target = Bukkit.getPlayer(friend);
                ItemStack head;
                if(target == null){
                    head = new ItemBuilder(Material.PLAYER_HEAD).setName("§b" + UUIDFetcher.getNameFromUUID(friend)).setLore("§c ", "§fStatut: §cHors-ligne").setSkullOwner(Bukkit.getOfflinePlayer(friend)).toItemStack();
                }
                else
                    {
                        if(ServerUnit.getByPort(target.getServer().getPort()).isPresent()){
                            String serverName = ServerUnit.getByPort(target.getServer().getPort()).get().getName();
                            head = new ItemBuilder(Material.PLAYER_HEAD).setName(target.getName()).setLore("§c ", "§fStatut: §aEn-ligne", "§fServeur: §6" + serverName).setSkullOwner(Bukkit.getPlayer(friend)).toItemStack();
                        }
                        else {
                            head = new ItemBuilder(Material.PLAYER_HEAD).setName(target.getName()).setLore("§c ", "§fStatut: §aEn-ligne", "§fServeur: §6Null").setSkullOwner(Bukkit.getPlayer(friend)).toItemStack();
                        }
                    }
                inv.setItem(a, head);
                a = a+1;
            }

            inv.setItem(53, new ItemBuilder(Material.ACACIA_DOOR).setName("§cQuitter").toItemStack());
            player.openInventory(inv);

        });

    }

    private static List<UUID> sortFriends(List<UUID> list){

        int count = 0;
        if(list.size() <= 1){
            return list;
        }
        for(UUID friend : list){
            UUID bfriend = list.get(count+1);
            Player fp = Bukkit.getPlayer(friend);
            Player fpb = Bukkit.getPlayer(bfriend);
            if(fpb != null && fp == null){
                list.remove(friend);
                list.add(friend);
            }
            else if(fpb == null && fp != null){
                continue;
            }
            else if(fp != null){
                continue;
            }
            count = count+1;
        }
        return list;
    }

}
