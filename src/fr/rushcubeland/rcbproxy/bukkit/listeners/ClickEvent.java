package fr.rushcubeland.rcbproxy.bukkit.listeners;

import fr.rushcubeland.rcbproxy.bukkit.BukkitSend;
import fr.rushcubeland.rcbproxy.bukkit.options.Options;
import fr.rushcubeland.rcbproxy.bukkit.sanction.SanctionGUI;
import fr.rushcubeland.rcbproxy.bukkit.sanction.SanctionUnit;
import fr.rushcubeland.rcbproxy.bukkit.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class ClickEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        e.getWhoClicked();
        if(e.getCurrentItem() == null){
            e.setCancelled(true);
            return;
        }
        if(e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();
            Inventory inventory = e.getInventory();
            InventoryView inventoryView = e.getView();
            String targetname = SanctionGUI.getModAndTarget().get(player);
            SanctionUnit sanction = null;
            if(inventoryView.getTitle().equalsIgnoreCase("§cPréférences")){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    player.closeInventory();
                }
                if(e.isLeftClick()){
                    if(e.getSlot() == 1){
                        if(e.getCurrentItem().getType().equals(Material.GRAY_DYE)){
                            Options.updateDataStateChat(player, "OPEN");
                            Options.updateDataStateChatProxy(player, "OPEN");
                            ItemStack chat = new ItemBuilder(Material.PAPER).setName("§6Afficher le chat").setLore("§fActiver ou désactiver le chat", "§c ", "§bActuellement: §aActivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                            inventory.setItem(1, state);
                            inventory.setItem(0, chat);
                        }
                        else {
                            Options.updateDataStateChat(player, "NEVER");
                            Options.updateDataStateChatProxy(player, "NEVER");
                            ItemStack chat = new ItemBuilder(Material.PAPER).setName("§6Afficher le chat").setLore("§fActiver ou désactiver le chat", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(1, state);
                            inventory.setItem(0, chat);
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1F, 1F);
                        return;
                    }
                    if(e.getSlot() == 4){
                        if(e.getCurrentItem().getType().equals(Material.GRAY_DYE)){
                            Options.updateDataStateFriendRequests(player, "OPEN");
                            Options.updateDataStateFriendRequestsProxy(player, "OPEN");
                            ItemStack friendRequests = new ItemBuilder(Material.PLAYER_HEAD).setName("§6Recevoir des requetes d'amis").setLore("§fActiver ou désactiver le fait de recevoir des requetes d'amis", "§c ", "§bActuellement: §aActivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                            inventory.setItem(4, state);
                            inventory.setItem(3, friendRequests);
                        }
                        else {
                            Options.updateDataStateFriendRequests(player, "NEVER");
                            Options.updateDataStateFriendRequestsProxy(player, "NEVER");
                            ItemStack friendRequests = new ItemBuilder(Material.PLAYER_HEAD).setName("§6Recevoir des requetes d'amis").setLore("§fActiver ou désactiver le fait de recevoir des requetes d'amis", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(4, state);
                            inventory.setItem(3, friendRequests);
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1F, 1F);
                        return;
                    }
                    if(e.getSlot() == 22){
                        if(e.getCurrentItem().getType().equals(Material.GRAY_DYE)){
                            Options.updateDataStateFriendsStatutNotif(player, "OPEN");
                            Options.updateDataStateFriendsStatutNotifProxy(player, "OPEN");
                            ItemStack notif = new ItemBuilder(Material.NETHER_STAR).setName("§6Notifications de co/deco des amis").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §aActivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §cDésactiver").toItemStack();
                            inventory.setItem(22, state);
                            inventory.setItem(21, notif);
                        }
                        else {
                            Options.updateDataStateFriendsStatutNotif(player, "NEVER");
                            Options.updateDataStateFriendsStatutNotifProxy(player, "NEVER");
                            ItemStack notif = new ItemBuilder(Material.NETHER_STAR).setName("§6Notifications de co/deco des amis").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(22, state);
                            inventory.setItem(21, notif);
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1F, 1F);
                        return;
                    }
                    if(e.getSlot() == 19){
                        if(e.getCurrentItem().getType().equals(Material.GRAY_DYE)){
                            Options.updateDataStateMP(player, "OPEN");
                            Options.updateDataStateMPProxy(player, "OPEN");
                            ItemStack mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §aActivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §eAmis uniquement").toItemStack();
                            inventory.setItem(19, state);
                            inventory.setItem(18, mp);
                        }
                        else if(e.getCurrentItem().getType().equals(Material.CYAN_DYE)){
                            Options.updateDataStateMP(player, "NEVER");
                            Options.updateDataStateMPProxy(player, "NEVER");
                            ItemStack mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(19, state);
                            inventory.setItem(18, mp);
                        }
                        else
                        {
                            Options.updateDataStateMP(player, "ONLY_FRIENDS");
                            Options.updateDataStateMPProxy(player, "ONLY_FRIENDS");
                            ItemStack mp = new ItemBuilder(Material.WRITABLE_BOOK).setName("§6Recevoir des messages privés").setLore("§fActiver ou désactiver le fait de recevoir des messages privés", "§c ", "§bActuellement: §eAmis uniquement").toItemStack();
                            ItemStack state = new ItemBuilder(Material.CYAN_DYE).setName("§bActuellement: §eAmis uniquement").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(19, state);
                            inventory.setItem(18, mp);
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1F, 1F);
                        return;
                    }
                    if(e.getSlot() == 37){
                        if(e.getCurrentItem().getType().equals(Material.GRAY_DYE)){
                            Options.updateDataStatePartyInvite(player, "OPEN");
                            Options.updateDataStatePartyInviteProxy(player, "OPEN");
                            ItemStack party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §aActivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.LIME_DYE).setName("§bActuellement: §aActivé").setLore("§c ", "§a> §fClic gauche pour §eAmis uniquement").toItemStack();
                            inventory.setItem(37, state);
                            inventory.setItem(36, party);
                        }
                        else if(e.getCurrentItem().getType().equals(Material.CYAN_DYE)){
                            Options.updateDataStatePartyInvite(player, "NEVER");
                            Options.updateDataStatePartyInviteProxy(player, "NEVER");
                            ItemStack party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §cDésactivé").toItemStack();
                            ItemStack state = new ItemBuilder(Material.GRAY_DYE).setName("§bActuellement: §cDésactivé").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(37, state);
                            inventory.setItem(36, party);
                        }
                        else
                        {
                            Options.updateDataStatePartyInvite(player, "ONLY_FRIENDS");
                            Options.updateDataStatePartyInviteProxy(player, "ONLY_FRIENDS");
                            ItemStack party = new ItemBuilder(Material.MINECART).setName("§6Recevoir des invitations de groupe").setLore("§fActiver ou désactiver le fait de recevoir des invitations de groupe", "§c ", "§bActuellement: §eAmis uniquement").toItemStack();
                            ItemStack state = new ItemBuilder(Material.CYAN_DYE).setName("§bActuellement: §eAmis uniquement").setLore("§c ", "§a> §fClic gauche pour §aActiver").toItemStack();
                            inventory.setItem(37, state);
                            inventory.setItem(36, party);
                        }
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1F, 1F);
                        return;
                    }
                }
            }
            if(SanctionGUI.getCurrentSanctionForATarget().containsKey(player)){
                sanction = SanctionGUI.getCurrentSanctionForATarget().get(player);
            }
            if(inventoryView.getTitle().equalsIgnoreCase("§d[Amis]")){
                e.setCancelled(true);
                if(e.getCurrentItem().getType().equals(Material.ACACIA_DOOR)){
                    player.closeInventory();
                }
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
                if(e.getCurrentItem().getType().equals(Material.DIAMOND_AXE)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.ABUS_REPORT);
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
                if(e.getCurrentItem().getType().equals(Material.GLASS_BOTTLE)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.FAUSSE_INFO);
                }
                if(e.getCurrentItem().getType().equals(Material.PUFFERFISH)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.FORMATAGE_INCORRECT);
                }
                if(e.getCurrentItem().getType().equals(Material.GOLDEN_SWORD)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.VENTARDISE);
                }
                if(e.getCurrentItem().getType().equals(Material.TNT)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.FLOOD_SPAM);
                }
                if(e.getCurrentItem().getType().equals(Material.ROTTEN_FLESH)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.MAUVAIS_LANGAGE);
                }
                if(e.getCurrentItem().getType().equals(Material.ZOMBIE_HEAD)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.PROVOCATION);
                }
                if(e.getCurrentItem().getType().equals(Material.CREEPER_HEAD)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.INSULTE);
                }
                if(e.getCurrentItem().getType().equals(Material.ENDER_PEARL)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.INCITATION_INFRACTION);
                }
                if(e.getCurrentItem().getType().equals(Material.BARRIER)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.LIEN_INTERDIT);
                }
                if(e.getCurrentItem().getType().equals(Material.BOOK)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.PUBLICITE);
                }
                if(e.getCurrentItem().getType().equals(Material.CARROT_ON_A_STICK)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.DDOS_HACK_LIEN);
                }
                if(e.getCurrentItem().getType().equals(Material.NAME_TAG)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.PSEUDO_INCORRECT);
                }
                if(e.getCurrentItem().getType().equals(Material.WITHER_SKELETON_SKULL)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.MENACE_IRL);
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
                if(e.getCurrentItem().getType().equals(Material.DIAMOND_SHOVEL)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.MACRO_CLICK);
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
                if(e.getCurrentItem().getType().equals(Material.FLINT_AND_STEEL)){
                    SanctionGUI.openConfirmSanctionGUI(player, targetname, SanctionUnit.ANTI_JEU);
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
