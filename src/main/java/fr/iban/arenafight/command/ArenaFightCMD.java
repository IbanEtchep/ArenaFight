package fr.iban.arenafight.command;

import fr.iban.arenafight.ArenaFightPlugin;
import fr.iban.arenafight.Kit;
import fr.iban.arenafight.SpawnPoint;
import fr.iban.arenafight.enums.Team;
import fr.iban.arenafight.manager.FightManager;
import fr.iban.arenafight.menu.ArenaFightMenu;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArenaFightCMD implements CommandExecutor, @Nullable TabCompleter {
    private final ArenaFightPlugin plugin;
    private FightManager manager;

    public ArenaFightCMD(ArenaFightPlugin arenaFightPlugin) {
        this.plugin = arenaFightPlugin;
        this.manager = plugin.getFightManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            Player player = (Player)sender;
            new ArenaFightMenu(player).open();
        }else if(args.length == 1){
            Player player = (Player)sender;
            if(args[0].equalsIgnoreCase("reload") && sender.hasPermission("arenafight.admin")){
                plugin.reloadConfig();
                sender.sendMessage("§aConfiguration reloaded");
            }else if(args[0].equalsIgnoreCase("join")){
                if(sender instanceof  Player){
                    manager.joinFightQueue(player);
                }
            }
        }else if(args.length >= 1){
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("arenafight.admin")) {
                    if (args[0].equalsIgnoreCase("kit")) {
                        if (args.length == 3) {
                            String name = args[2];
                            switch (args[1]) {
                                case "add": {
                                    Kit kit = new Kit(player.getInventory());
                                    kit.setDisplayName(name);
                                    plugin.getConfig().set("kits." + name, kit);
                                    plugin.saveConfig();
                                    player.sendMessage("§aLe kit a bien été ajouté.");
                                    break;
                                }
                                case "delete":
                                    plugin.getConfig().set("kits." + name, null);
                                    plugin.saveConfig();
                                    player.sendMessage("§cLe kit a bien été supprimé.");
                                    break;
                                case "get": {
                                    Kit kit = (Kit) plugin.getConfig().get("kits." + name);
                                    kit.giveToPlayer(player);
                                    break;
                                }
                                default:
                                    player.sendMessage("/arenafight kit add/delete/get <name>");
                                    break;
                            }
                        } else {
                            player.sendMessage("/arenafight kit add/delete/get <name>");
                        }
                    }
                    if (args.length == 3) {
                        //arenafight spawnpoint team addDoorBlock
                        //arenafight spawnpoint team setSpawnLocation
                        if (args[0].equalsIgnoreCase("spawnpoint")) {
                            Team team = Team.valueOf(args[1].toUpperCase());
                            SpawnPoint spawnPoint = plugin.getFightManager().getArena().getSpawnPoints().get(team);
                            if (args[2].equalsIgnoreCase("addDoorBlock")) {
                                spawnPoint.getDoorLocations().add(player.getTargetBlock(null, 200).getLocation());
                                player.sendMessage("§aBloc ajouté aux doorLocations");
                            }
                            if (args[2].equalsIgnoreCase("setSpawnLocation")) {
                                spawnPoint.setSpawnLocation(player.getLocation());
                                player.sendMessage("§aLe point d'apparition a bien été redéfini.");
                            }
                            plugin.getConfig().set("arena.spawn-points." + team, spawnPoint);
                            plugin.saveConfig();
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
