package fr.rushcubeland.rcbproxy.bukkit.mod;

import fr.rushcubeland.rcbproxy.bukkit.RcbProxy;
import fr.rushcubeland.rcbproxy.bukkit.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

public class ModModerator {

    private static ArrayList<String> playersMod = new ArrayList<>();

    public static void removeMod(String targetUUID){
        if(playersMod.contains(targetUUID)){
            playersMod.remove(targetUUID);
            deleteTools(targetUUID);
            Player mod = Bukkit.getPlayer(UUID.fromString(targetUUID));
            if(mod != null){
                for(Player pls : Bukkit.getOnlinePlayers()){
                    pls.showPlayer(RcbProxy.getInstance(), mod);
                }
            }
        }
    }

    public static void addMod(String targetUUID){
        if(!playersMod.contains(targetUUID)){
            playersMod.add(targetUUID);
            giveTools(targetUUID);
            Player mod = Bukkit.getPlayer(UUID.fromString(targetUUID));
            if(mod != null){
                for(Player pls : Bukkit.getOnlinePlayers()){
                    pls.hidePlayer(RcbProxy.getInstance(), mod);
                }
            }
        }
    }

    public static boolean isInModData(String targetUUID){
        if(playersMod.contains(targetUUID)){
            return true;
        }
        return false;
    }

    public static void giveTools(String targetUUID){
        Player player = Bukkit.getPlayer(UUID.fromString(targetUUID));
        if(player != null){
            player.getInventory().clear();
            ItemStack kb = new ItemBuilder(Material.WOODEN_SWORD).setName("§aEpée de KB").setInfinityDurability().toItemStack();
            ItemMeta meta = kb.getItemMeta();
            meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
            kb.setItemMeta(meta);
            player.getInventory().setItem(1, kb);
        }

    }

    public static void deleteTools(String targetUUID){
        Player player = Bukkit.getPlayer(UUID.fromString(targetUUID));
        if(player != null){
            player.getInventory().clear();
        }
    }

    public static ArrayList<String> getPlayersMod() {
        return playersMod;
    }
}
