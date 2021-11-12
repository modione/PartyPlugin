package me.modione.partyplugin.commands;

import me.modione.partyplugin.PartyPlugin;
import me.modione.partyplugin.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DropPartyCommand implements TabExecutor, Listener {
    private Inventory inventory;
    private ItemStack stack = new ItemBuilder(Material.GREEN_STAINED_GLASS)
            .displayname(ChatColor.GREEN+"Start Drop-Party")
            .lore("This will start the Drop-Party")
            .build();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PartyPlugin.PREFIX+" Command only usable as a Player!");
            return false;
        }
        Player player = (Player) sender;
        if (args[0].equals("throw")) {
            inventory = Bukkit.createInventory(null, 36, "");
            inventory.setItem(35, stack);
            player.openInventory(inventory);
            return true;
        }
        player.sendMessage("Parameter");
        player.sendMessage("    throw: Lets you throw a Drop-Party");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> ret = new ArrayList<>();
        ret.add("throw");
        return ret;
    }

    @EventHandler
    public void on_Inv_Click(InventoryClickEvent event) {
        if (inventory == null) return;
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem()==null) return;
        if (!Objects.equals(event.getClickedInventory(), inventory)) return;
        if (!Objects.equals(event.getCurrentItem(), stack)) return;
        event.setCancelled(true);
        event.getWhoClicked().closeInventory();
        event.getClickedInventory().setItem(35, null);
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : event.getClickedInventory()) {
            if (item==null) continue;
            if (item==stack) continue;
            items.add(item);
        }
        throw_DropParty(items, (Player) event.getWhoClicked());

    }

    public void throw_DropParty(List<ItemStack> items2throw, Player host) {
        Bukkit.broadcastMessage(PartyPlugin.INSTANCE.broadcast.replace("$PLAYER$", host.getDisplayName()));
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        while (!items2throw.isEmpty()) {
            for (Player player : players) {
                if (items2throw.isEmpty()) return;
                if (player==host) continue;
                ItemStack stack = items2throw.get(0);
                if (player.getInventory().addItem(stack).isEmpty()) {
                    String itemName = stack.getItemMeta().hasDisplayName() ? stack.getItemMeta().getDisplayName() : stack.getType().name().toLowerCase().replace("_", " ");
                    player.sendMessage(PartyPlugin.INSTANCE.playermsg.replace("$PLAYER$",
                            host.getDisplayName()).replace("$ITEM$",itemName));
                    items2throw.remove(stack);
                }
            }
        }
    }

}
