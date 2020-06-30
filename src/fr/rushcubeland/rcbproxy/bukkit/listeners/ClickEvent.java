package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.BukkitSend;
import fr.rushcubeland.rcbproxy.bukkit.sanction.SanctionGUI;
import fr.rushcubeland.rcbproxy.bukkit.sanction.SanctionUnit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getWhoClicked() == null || e.getInventory() == null){
            e.setCancelled(true);
            return;
        }
        if(e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();
            Inventory inventory = e.getInventory();
            InventoryView inventoryView = e.getView();
            String targetname = SanctionGUI.getModAndTarget().get(player);
            SanctionUnit sanction = null;
            if(SanctionGUI.getCurrentSanctionForATarget().containsKey(player)){
                sanction = SanctionGUI.getCurrentSanctionForATarget().get(player);
            }
            if(inventoryView.getTitle().equalsIgnoreCase("§6Sanction §e> §c" + targetname)){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    player.closeInventory();
                }
                if(e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
                }
                if(e.getCurrentItem().getType().equals(Material.PAPER)){
                    SanctionGUI.openMsgGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.IRON_SWORD)){
                    SanctionGUI.openGameplayGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)){
                    SanctionGUI.openTricheGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.LAVA_BUCKET)){
                    SanctionGUI.openAbusGui(player, targetname);
                }
            }
            if(inventoryView.getTitle().equalsIgnoreCase("§6Sanction Abus §e> §c" + targetname)){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    SanctionGUI.openModGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.PUMPKIN)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.TROLL);
                }
            }
            if(inventoryView.getTitle().equalsIgnoreCase("§6Sanction Messages §e> §c" + targetname)){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    SanctionGUI.openModGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.WOODEN_HOE)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.MESSAGES_INUTILE);
                }
            }
            if(inventoryView.getTitle().equalsIgnoreCase("§6Sanction Triche §e> §c" + targetname)){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    SanctionGUI.openModGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.COBWEB)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.ANTI_KB);
                }
                if(e.getCurrentItem().getType().equals(Material.IRON_SWORD)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.KILL_AURA);
                }
                if(e.getCurrentItem().getType().equals(Material.CLOCK)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.FAST_PLACE);
                }
                if(e.getCurrentItem().getType().equals(Material.BOW)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.REACH);
                }
                if(e.getCurrentItem().getType().equals(Material.FEATHER)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.FLY);
                }
                if(e.getCurrentItem().getType().equals(Material.STONE)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.TRICHE_AUTRE);
                }
            }
            if(inventoryView.getTitle().equalsIgnoreCase("§6Sanction Gameplay §e> §c" + targetname)){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    SanctionGUI.openModGui(player, targetname);
                }
                if(e.getCurrentItem().getType().equals(Material.TROPICAL_FISH)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.ALLY);
                }
                if(e.getCurrentItem().getType().equals(Material.LEATHER_CHESTPLATE)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.SKIN_INCORRECT);
                }
            }
            if(sanction != null){
                if(inventoryView.getTitle().equalsIgnoreCase("§6Sanction "+ sanction.getMotif() + " §e> §c" + targetname)){
                    e.setCancelled(true);
                    if(e.getCurrentItem().getType().equals(Material.RED_DYE)){
                        SanctionGUI.removeSanction(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_WANDERING_TRADER_NO, 1F, 1F);
                        SanctionGUI.openModGui(player, targetname);
                    }
                    if(e.getCurrentItem().getType().equals(Material.LIME_DYE)){
                        if(sanction.getSanctionCmd().equalsIgnoreCase("Mute")){
                            BukkitSend.muteToProxy(player, targetname, sanction.getDurationSeconds(), sanction.getMotif());
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
                            player.sendMessage("§6[AP] §aApplication de la sanction effectuée !");
                        }
                        if(sanction.getSanctionCmd().equalsIgnoreCase("Ban")){
                            BukkitSend.banToProxy(player, targetname, sanction.getDurationSeconds(), sanction.getMotif());
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
                            player.sendMessage("§6[AP] §aApplication de la sanction effectuée !");
                        }
                    }
                }
            }
        }
    }

}
