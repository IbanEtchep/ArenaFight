package fr.iban.arenafight.enums;

import org.bukkit.Color;

public enum Team {

    RED(Color.RED),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW);

    private final Color color;

    Team(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
