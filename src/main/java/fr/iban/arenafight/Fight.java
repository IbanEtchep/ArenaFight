package fr.iban.arenafight;

import fr.iban.arenafight.enums.GameState;
import fr.iban.arenafight.enums.Team;
import fr.iban.arenafight.task.StartTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;

public class Fight {

    private final ArenaFightPlugin plugin;
    private final Map<UUID, Kit> savedInventories = new HashMap<>();
    private GameState gameState = GameState.WAITING;
    private final Arena arena;
    private final Map<Team, Collection<ArenaPlayer>> teams = new HashMap<>();
    private final Map<UUID, ArenaPlayer> players = new HashMap<>();
    private Kit kit;
    private int rounds = 1;
    private int currentRound = 1;
    private UUID host;


    public Fight(ArenaFightPlugin plugin, Arena arena) {
        this.arena = arena;
        this.plugin = plugin;
        addTeam();
        addTeam();
    }

    public Fight(ArenaFightPlugin plugin, Arena arena , UUID host) {
        this(plugin, arena);
        this.host = host;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void start(){
        gameState = GameState.RUNNING;
        arena.setHasFightRunning(true);
        for(ArenaPlayer arenaPlayer : getArenaPlayers().values()){
            Player player = arenaPlayer.getPlayer();
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
            arenaPlayer.saveInventory();
            Location spawnLoc = arena.getSpawnPoints().get(arenaPlayer.getTeam()).getSpawnLocation();
            player.teleportAsync(spawnLoc);
            if(kit != null){
                kit.giveToPlayer(player);
            }
        }
        new StartTask(this, () -> {
            for(Team team : teams.keySet()){
                arena.getSpawnPoints().get(team).openDoor();
            }
        }).runTaskTimer(plugin, 20L, 20L);
    }

    public void finish(){
        gameState = GameState.FINISHED;
        players.values().forEach(arenaPlayer -> {
            Player player = arenaPlayer.getPlayer();
            if(player != null){
                if(arenaPlayer.isAlive()){
                    player.teleport(plugin.getFightManager().getWaitingPoint());
                    arenaPlayer.restoreInventory();
                }
                if(getAliveTeams().contains(arenaPlayer.getTeam())){
                    player.showTitle(Title.title(Component.text("Victoire !").color(TextColor.fromCSSHexString("#27ae60")), Component.text("")));
                }else{
                    player.showTitle(Title.title(Component.text("Défaite :(").color(TextColor.fromCSSHexString("#e74c3c")), Component.text("")));
                }
            }
        });
        for(Team team : teams.keySet()){
            arena.getSpawnPoints().get(team).closeDoor();
        }
        arena.setHasFightRunning(false);
    }


    public boolean canStart(){
        return arePlayersReady()
                && players.size() >= 2
                && players.size()%teams.size() == 0
                && !arena.hasFightRunning()
                && teams.size() >= 2
                && areTeamsFilled();
    }

    public void addTeam(){
        if(gameState == GameState.WAITING && teams.size() < arena.getSpawnPoints().size()){
            for(Team team : Team.values()){
                if(!teams.containsKey(team)){
                    teams.put(team, new ArrayList<>());
                    break;
                }
            }
        }
    }

    public void addPlayer(Player player){
        players.put(player.getUniqueId(), new ArenaPlayer(player));
        fillTeams();
    }

    public Map<UUID, ArenaPlayer> getArenaPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void addPlayerToTeam(ArenaPlayer arenaPlayer, Team team){
        if(teams.containsKey(team)){
            teams.get(team).add(arenaPlayer);
            arenaPlayer.setTeam(team);
        }
    }

    public Map<Team, Collection<ArenaPlayer>> getTeams() {
        return teams;
    }

    public void fillTeams(){
        for(ArenaPlayer arenaPlayer : getArenaPlayers().values()){
            if(arenaPlayer.getTeam() == null){
                addPlayerToTeam(arenaPlayer, getLessPlayerTeam());
            }
        }
    }

    public boolean areTeamsFilled(){
        for(ArenaPlayer arenaPlayer : getArenaPlayers().values()){
            if(arenaPlayer.getPlayer() == null || arenaPlayer.getTeam() == null ){
                return false;
            }
        }
        return Collections.min(teams.entrySet(), Comparator.comparingInt(a -> a.getValue().size())).getKey() == Collections.max(teams.entrySet(), Comparator.comparingInt(a -> a.getValue().size())).getKey();
    }

    private Team getLessPlayerTeam(){
        return Collections.min(teams.entrySet(), Comparator.comparingInt(a -> a.getValue().size())).getKey();
    }

    private boolean isAlive(Team team){
        for(ArenaPlayer arenaPlayer : teams.get(team)){
            if(arenaPlayer.isAlive()){
                return true;
            }
        }
        return false;
    }

    public List<Team> getAliveTeams(){
        return teams.keySet().stream().filter(this::isAlive).toList();
    }

    public UUID getHost() {
        return host;
    }


    /*
    DEATH
     */

    public void handleDeath(UUID uuid){
        if(gameState == GameState.RUNNING){
            ArenaPlayer arenaPlayer = players.get(uuid);
            Player player = arenaPlayer.getPlayer();
            arenaPlayer.setAlive(false);
            player.teleport(plugin.getFightManager().getWaitingPoint());
            arenaPlayer.restoreInventory();
            if(getAliveTeams().size() == 1){
                finish();
            }
        }
    }

    public Arena getArena() {
        return arena;
    }

    private boolean arePlayersReady() {
        for (ArenaPlayer arenaPlayer : players.values()) {
            Player player = arenaPlayer.getPlayer();
            if(!arenaPlayer.isOnline() || player.getOpenInventory().getType() == InventoryType.SHULKER_BOX) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Fight{" +
                "gameState=" + gameState +
                ", arena=" + arena +
                ", teams=" + teams +
                ", players=" + players +
                ", kit=" + kit +
                ", rounds=" + rounds +
                ", currentRound=" + currentRound +
                ", savedInventories=" + savedInventories +
                ", host=" + host +
                '}';
    }
}
