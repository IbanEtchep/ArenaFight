package fr.iban.arenafight.listener;

import fr.iban.arenafight.Fight;
import fr.iban.arenafight.manager.FightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerDropItemListener implements Listener {

    private FightManager fightManager;

    public PlayerDropItemListener(FightManager fightManager) {
        this.fightManager = fightManager;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        Fight fight = fightManager.getPlayingFight(player);
        if(fight != null){
            e.setCancelled(true);
        }
    }
}
