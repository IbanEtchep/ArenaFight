package fr.iban.arenafight;

import fr.iban.arenafight.enums.Team;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Arena {

    private Map<Team, SpawnPoint> spawnPoints = new HashMap<>();
    private boolean hasFightRunning = false;

    public Map<Team, SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    public boolean hasFightRunning() {
        return hasFightRunning;
    }

    public void setHasFightRunning(boolean hasFightRunning) {
        this.hasFightRunning = hasFightRunning;
    }

    @Override
    public String toString() {
        return "Arena{" +
                "spawnPoints=" + spawnPoints +
                ", hasFightRunning=" + hasFightRunning +
                '}';
    }
}
