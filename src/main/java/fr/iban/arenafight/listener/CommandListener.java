package fr.iban.arenafight.listener;

import fr.iban.arenafight.ArenaFightPlugin;
import fr.iban.arenafight.Fight;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    private ArenaFightPlugin plugin;

    public CommandListener(ArenaFightPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        Player player = e.getPlayer();
        Fight fight = plugin.getFightManager().getPlayingFight(player);
        if(fight != null){
            e.setCancelled(true);
        }
    }

}
