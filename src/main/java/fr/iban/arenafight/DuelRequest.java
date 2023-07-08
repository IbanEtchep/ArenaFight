package fr.iban.arenafight;

import org.bukkit.entity.Player;

public class DuelRequest {

    private final Player playerFrom;
    private final Player playerTo;
    private Kit kit;

    public DuelRequest(Player playerFrom, Player playerTo, Kit kit) {
        this.playerFrom = playerFrom;
        this.playerTo = playerTo;
        this.kit = kit;
    }

    public Player getPlayerFrom() {
        return playerFrom;
    }

    public Player getPlayerTo() {
        return playerTo;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }
}
