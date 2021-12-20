package fr.iban.arenafight.listener;

import fr.iban.arenafight.Fight;
import fr.iban.arenafight.manager.FightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private FightManager fightManager;

    public PlayerQuitListener(FightManager fightManager) {
        this.fightManager = fightManager;
    }

    @EventHandler
    public void onDeath(PlayerQuitEvent e){
        Player player = e.getPlayer();

        Fight fight = fightManager.getPlayingFight(player);
        if(fight != null){
            fight.handleDeath(player.getUniqueId());
        }
    }

}
