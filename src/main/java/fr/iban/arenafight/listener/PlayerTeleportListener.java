package fr.iban.arenafight.listener;

import fr.iban.arenafight.Fight;
import fr.iban.arenafight.manager.FightManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    private FightManager fightManager;

    public PlayerTeleportListener(FightManager fightManager) {
        this.fightManager = fightManager;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Player player = e.getPlayer();
        Fight fight = fightManager.getPlayingFight(player);
        if(fight != null){
            if(e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.getCause() != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT){
                fight.handleDeath(player.getUniqueId());
            }
        }
    }
}
