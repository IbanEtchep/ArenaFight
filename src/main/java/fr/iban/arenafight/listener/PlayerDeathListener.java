package fr.iban.arenafight.listener;

import fr.iban.arenafight.ArenaFightPlugin;
import fr.iban.arenafight.Fight;
import fr.iban.arenafight.manager.FightManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private ArenaFightPlugin plugin;

    public PlayerDeathListener(ArenaFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        Fight fight = plugin.getFightManager().getPlayingFight(player);
        if(fight != null){
            e.setKeepLevel(true);
            e.setShouldDropExperience(false);
            e.getDrops().clear();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.spigot().respawn();
                fight.handleDeath(player.getUniqueId());
            },1L);
        }
    }

}
