package fr.iban.arenafight.manager;

import fr.iban.arenafight.*;
import fr.iban.arenafight.enums.GameState;
import fr.iban.arenafight.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FightManager {

    private ArenaFightPlugin plugin;
    private Arena arena;
    private Location waitingPoint;

    private Fight currentFight;
    private List<Fight> waitingList = new ArrayList<>();

    public FightManager(ArenaFightPlugin plugin) {
        this.plugin = plugin;
        arena = getArenaFromConfig();
        waitingPoint = getWaitingPointFromConfig();
        checkFinishAndStartNext();
    }


    private void checkFinishAndStartNext() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            if(currentFight != null && currentFight.getGameState() == GameState.FINISHED){
                currentFight = null;
            }

            if(currentFight == null && !waitingList.isEmpty()){
                for (Fight fight : waitingList){
                    if(fight.canStart()){
                        fight.start();
                        currentFight = fight;
                        waitingList.remove(fight);
                        break;
                    }
                }
            }

        }, 20L, 20L);
    }

    public void joinFightQueue(Player player){
        if(getPlayingFight(player) == null){
            getJoinablePublicFight().addPlayer(player);
            player.sendMessage("§aVous avez été ajouté à la fille d'attente des duels.");
        }else{
            player.sendMessage("§cVous êtes déjà inscrits à un combat.");
        }
    }

    private Fight getJoinablePublicFight(){
        for(Fight fight : waitingList){
            if(fight.getHost() == null && fight.getArenaPlayers().size() <= 1){
                return fight;
            }
        }
        Fight fight = new Fight(plugin, arena);
        waitingList.add(fight);
        return fight;
    }

    public void addDuelToQueue(DuelRequest request){
        Fight fight = new Fight(plugin, arena);
        fight.addPlayer(request.getPlayerFrom());
        fight.addPlayer(request.getPlayerTo());
        fight.setKit(request.getKit());
        waitingList.add(fight);
    }

    /**
     * @param player
     * @return fight the player is currently playing.
     */
    public Fight getPlayingFight(Player player){
        if(currentFight != null){
            if(currentFight.getArenaPlayers().containsKey(player.getUniqueId())){
                if(currentFight.getArenaPlayers().get(player.getUniqueId()).isAlive()){
                    return currentFight;
                }
            }
        }
        return null;
    }


    public Arena getArena() {
        return arena;
    }

    public Location getWaitingPoint() {
        return waitingPoint;
    }

    public List<Fight> getWaitingList() {
        return waitingList;
    }

    public Fight getCurrentFight() {
        return currentFight;
    }

    private Arena getArenaFromConfig(){
        Arena arena = new Arena();
        for(String key : plugin.getConfig().getConfigurationSection("arena.spawn-points").getKeys(false)){
            try {
                Team team = Team.valueOf(key);
                SpawnPoint spawnPoint = (SpawnPoint) plugin.getConfig().get("arena.spawn-points."+key);
                arena.getSpawnPoints().put(team, spawnPoint);
            }catch (IllegalArgumentException e){
                plugin.getLogger().warning("Erreur lors du chargement d'un point d'apparition : " + key + " ne fait pas parti des teams disponibles.");
            }
        }
        return arena;
    }

    private Location getWaitingPointFromConfig(){
        return plugin.getConfig().getLocation("waiting-location");
    }
}
