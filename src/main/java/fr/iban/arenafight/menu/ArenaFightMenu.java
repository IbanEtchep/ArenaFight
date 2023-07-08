package fr.iban.arenafight.menu;

import fr.iban.arenafight.ArenaFightPlugin;
import fr.iban.arenafight.ArenaPlayer;
import fr.iban.arenafight.Fight;
import fr.iban.arenafight.enums.Team;
import fr.iban.arenafight.manager.FightManager;
import fr.iban.menuapi.menuitem.MenuItem;
import fr.iban.menuapi.menu.LazyPaginatedMenu;
import fr.iban.menuapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArenaFightMenu extends LazyPaginatedMenu<Fight> {

    public ArenaFightMenu(Player player) {
        super(player);
    }

    @Override
    protected int[] getFillableSlots() {
        return new int[] {10,11,12,13,14,15,16,17};
    }

    @Override
    protected MenuItem getMenuItem(Fight fight) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.FILLED_MAP).setName("§8Combat");
        for(Map.Entry<Team, Collection<ArenaPlayer>> team : fight.getTeams().entrySet()){
            if(!team.getValue().isEmpty()){
                itemBuilder.addLore("§8"+team.getKey().toString() + " :");
                for(ArenaPlayer arenaPlayer : team.getValue()){
                    itemBuilder.addLore("§7   - " + arenaPlayer.getPlayer().getName());
                }
            }
        }
        return new MenuItem(-1, itemBuilder.build());
    }

    @Override
    protected List<Fight> getLazyObjectList() {
        return new ArrayList<>(ArenaFightPlugin.getInstance().getFightManager().getWaitingList());
    }

    @Override
    public String getMenuName() {
        return "Menu des combats";
    }

    @Override
    public int getRows() {
        return 6;
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
    }
}
