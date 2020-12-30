package seFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {
    Location cLocation;
    boolean operate = false;
    
    @Override
    public void onEnable() {
        System.out.println("SeFactory is activated");

        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("factory").setExecutor(this::onCommand);
        getCommand("factory").setTabCompleter(this::onTabComplete);
    }

    @Override
    public void onDisable() {
        System.out.println("SeFactory is deactivated");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("factory")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GOLD + "We write it 'automation' and read it 'labor'");
                return false;
            }
            else if (args[0].equalsIgnoreCase("give")) {
                if (args[1].equalsIgnoreCase("@all")) {
                    return false;
                }
                else if (args[1].equalsIgnoreCase("@local")) {
                    ItemStack stack = new ItemStack(Material.SMOOTH_STONE_SLAB);
                    ItemMeta meta = stack.getItemMeta();

                    meta.setDisplayName("conveyor");
                    stack.setItemMeta(meta);

                    player.getInventory().addItem(stack);

                    return false;
                }
                else if (args[2].equalsIgnoreCase("@random")) {
                    return false;
                }
                else {
                    return false;
                }
            }
            else if (args[0].equalsIgnoreCase("operate")) {
                if (args[1].equalsIgnoreCase("start")) {
                    if (!operate) {
                        player.sendMessage(ChatColor.GOLD + "factory operate: " + ChatColor.AQUA + "start");
                        operate = true;
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "factory has already started to operate");
                    }
                }
                else if (args[1].equalsIgnoreCase("stop")) {
                    if (operate) {
                        player.sendMessage(ChatColor.GOLD + "factory operate: " + ChatColor.AQUA + "stop");
                        operate = false;
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "factory has already stopped to operate");
                    }
                }
            }
            else {
                return false;
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
                type.add("operate");

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
                else if (args[0].equalsIgnoreCase("operate")) {
                    List<String> type = new ArrayList<>();

                    type.add("start");
                    type.add("stop");

                    return type;
                }
            }
        }
        return null;
    }

    @EventHandler
    public void itemHeld(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();

        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("conveyor")) {
            player.sendMessage("good");
            FallingBlock block = player.getWorld().spawnFallingBlock(player.getTargetBlock(null, 50).getLocation(), Material.POLISHED_DIORITE_SLAB, (byte) 0);
            block.setGravity(false);
        }
        else {
            player.sendMessage("good");
        }
    }
    
    @EventHandler
    public void onSwapItemInHand(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();

        if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("conveyor")) {
            cLocation = player.getTargetBlock(null, 50).getLocation();
            cLocation.getBlock().setType(Material.SMOOTH_STONE_SLAB);
            e.setCancelled(true);
        }
    }
}
