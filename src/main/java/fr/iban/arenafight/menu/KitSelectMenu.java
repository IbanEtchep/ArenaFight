package fr.iban.arenafight.menu;

import fr.iban.arenafight.ArenaFightPlugin;
import fr.iban.arenafight.Kit;
import fr.iban.menuapi.menu.LazyPaginatedMenu;
import fr.iban.menuapi.menu.Menu;
import fr.iban.menuapi.menu.PaginatedMenu;
import fr.iban.menuapi.menuitem.MenuItem;
import fr.iban.menuapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class KitSelectMenu extends LazyPaginatedMenu<Kit> {

    private final Consumer<Kit> kitConsumer;

    public KitSelectMenu(Player player, Consumer<Kit> kitConsumer) {
        super(player);
        this.kitConsumer = kitConsumer;
    }

    @Override
    protected int[] getFillableSlots() {
        return new int[] {0,1,2,3,4,5,6,7,8};
    }

    @Override
    protected MenuItem getMenuItem(Kit kit) {
        return new MenuItem(-1, new ItemBuilder(Material.CHEST).setName("§e"+kit.getDisplayName()).build()).setClickCallback(e -> {
            player.closeInventory();
            kitConsumer.accept(kit);
        });
    }

    @Override
    protected List<Kit> getLazyObjectList() {
        List<Kit> kits = new ArrayList<>();
        FileConfiguration config = ArenaFightPlugin.getInstance().getConfig();
        for(String key : config.getConfigurationSection("kits").getKeys(false)){
            kits.add((Kit) config.get("kits." + key));
        }
        return kits;
    }

    @Override
    public String getMenuName() {
        return "Choisissez un kit";
    }

    @Override
    public int getRows() {
        return 3;
    }

    @Override
    public void setMenuItems() {
        addMenuItem(new MenuItem(26, new ItemBuilder(Material.ANVIL)
                .setName("§6Inventaire actuel").addLore("§eBattez-vous avec votre inventaire actuel.")
                .addLore("§7Tous les participants auront votre inventaire.")
                .build())
                .setClickCallback(e -> {
                    Kit kit = new Kit(player.getInventory());
                    kit.setDisplayName("Inventaire de "+player.getName());
                    player.closeInventory();
                    kitConsumer.accept(kit);
                }));
        addMenuItem(getNextBotton());
        addMenuItem(getPreviousBotton());
    }
}
