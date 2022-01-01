package fr.iban.arenafight;

import fr.iban.arenafight.command.ArenaFightCMD;
import fr.iban.arenafight.command.DuelCMD;
import fr.iban.arenafight.listener.CommandListener;
import fr.iban.arenafight.listener.PlayerDeathListener;
import fr.iban.arenafight.listener.PlayerQuitListener;
import fr.iban.arenafight.manager.FightManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArenaFightPlugin extends JavaPlugin {

    private FightManager fightManager;
    private static ArenaFightPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ConfigurationSerialization.registerClass(Kit.class, "Kit");
        ConfigurationSerialization.registerClass(SpawnPoint.class, "SpawnPoint");
        fightManager = new FightManager(this);
        getCommand("arenafight").setExecutor(new ArenaFightCMD(this));
        getCommand("arenafight").setTabCompleter(new ArenaFightCMD(this));
        getCommand("duel").setExecutor(new DuelCMD(this));

        registerListeners(new PlayerDeathListener(this), new PlayerQuitListener(fightManager),new CommandListener(this)
        );
    }

    @Override
    public void onDisable() {
        fightManager.getArena().getSpawnPoints().forEach((team, spawnPoint) -> spawnPoint.closeDoor());
        fightManager.getCurrentFight().getArenaPlayers().forEach((uuid, arenaPlayer) -> arenaPlayer.restoreInventory());
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pm = Bukkit.getPluginManager();

        for (Listener listener : listeners) {
            pm.registerEvents(listener, this);
        }

    }

    public FightManager getFightManager() {
        return fightManager;
    }

    public static ArenaFightPlugin getInstance() {
        return instance;
    }
}
