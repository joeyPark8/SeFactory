package seFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    Location cLocation;
    
    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("factory")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "We write it 'automation' and read it 'labor'");
            }
            else if (args[0].equalsIgnoreCase("give")) {
                player.getInventory().addItem(new ItemStack(Material.SMOOTH_STONE_SLAB));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("factory")) {
            if (args.length == 1) {
                List<String> type = new ArrayList<>();

                type.add("give");

                return type;
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    List<String> targets = new ArrayList<>();
                    Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);

                    for (int i = 0; i < players.length; i++) {
                        targets.add(players[i].getName());
                    }

                    targets.add("@all");
                    targets.add("@local");
                    targets.add("@random");

                    return targets;
                }
            }
        }
        return null;
    }

    @EventHandler
    public void itemHeld(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();

        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("conveyor")) {
            cLocation = new Location(player.getWorld(), (int)player.getEyeLocation().getX(), (int)player.getEyeLocation().getY(), (int)player.getEyeLocation().getZ());
            player.getWorld().spawnFallingBlock(cLocation, Material.POLISHED_DIORITE_SLAB, (byte) 0);
        }
    }
    
    @EventHandler
    public void onSwapItemInHand(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();

        if (player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equalsIgnoreCase("conveyor")) {
            cLocation.getBlock().setType(Material.SMOOTH_STONE_SLAB);
            e.setCancelled(true);
        }
    }
}
