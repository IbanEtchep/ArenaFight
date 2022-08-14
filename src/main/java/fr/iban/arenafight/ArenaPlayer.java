package fr.iban.arenafight;

import fr.iban.arenafight.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class ArenaPlayer {

    private UUID uuid;
    private Kit savedInventory;
    private Team team;
    private boolean alive = true;

    public ArenaPlayer(Player player) {
        this.uuid = player.getUniqueId();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    public void saveInventory(){
        PlayerInventory inventory = getPlayer().getInventory();
        savedInventory = new Kit(inventory.getArmorContents(), inventory.getStorageContents(), inventory.getExtraContents());
    }

    public void restoreInventory(){
        getPlayer().closeInventory();
        savedInventory.giveToPlayer(getPlayer());
        getPlayer().setHealth(getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }
}
