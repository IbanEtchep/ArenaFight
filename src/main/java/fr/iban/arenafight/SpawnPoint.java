package fr.iban.arenafight;

import fr.iban.arenafight.enums.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("SpawnPoint")
public class SpawnPoint implements ConfigurationSerializable {

    private List<Location> doorLocations = new ArrayList<>();
    private Map<Location, BlockData> blockDataMap = new HashMap<>();
    private Location spawnLocation;

    public SpawnPoint(Map<String, Object> map){
        if(map.containsKey("doorLocations")){
            this.doorLocations = (List<Location>) map.get("doorLocations");
        }
        if(map.containsKey("spawnLocation")){
            this.spawnLocation = (Location) map.get("spawnLocation");
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public List<Location> getDoorLocations() {
        return doorLocations;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public void openDoor(){
        for (Location loc : getDoorLocations()) {
            blockDataMap.put(loc, loc.getBlock().getBlockData());
        }
        for(Location loc : getDoorLocations()){
            loc.getBlock().setType(Material.AIR);
            loc.getWorld().spawnParticle(Particle.CLOUD, loc, 50, 1,1,1,1);
        }
    }

    public void closeDoor(){
        for(Location loc : getDoorLocations()){
            loc.getBlock().setType(Material.IRON_BARS);
            loc.getBlock().setBlockData(blockDataMap.get(loc));
            loc.getWorld().spawnParticle(Particle.CLOUD, loc, 50, 1,1,1,1);
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if(doorLocations != null && !doorLocations.isEmpty()){
            map.put("doorLocations", doorLocations);
        }
        if(spawnLocation != null){
            map.put("spawnLocation", spawnLocation);
        }
        return map;
    }
}
