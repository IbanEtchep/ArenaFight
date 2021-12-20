package fr.iban.arenafight;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {

    private String displayName;
    private ItemStack[] armorContent; //4
    private ItemStack[] storageContent; //36
    private ItemStack[] extraContent; //1

    public Kit(ItemStack[] armorContent, ItemStack[] storageContent, ItemStack[] extraContent) {
        this.armorContent = armorContent;
        this.storageContent = storageContent;
        this.extraContent = extraContent;
    }

    public Kit(Map<String, Object> map){
        if(map.containsKey("displayName")){
            this.displayName = (String) map.get("displayName");
        }
        if(map.containsKey("armorContent")){
            List<ItemStack> armorContentList = (List<ItemStack>) map.get("armorContent");
            armorContent = armorContentList.toArray(new ItemStack[4]);
        }
        if(map.containsKey("storageContent")){
            List<ItemStack> storageContentList = (List<ItemStack>) map.get("storageContent");
            storageContent = storageContentList.toArray(new ItemStack[36]);
        }
        if(map.containsKey("extraContent")){
            List<ItemStack> extraContentList = (List<ItemStack>) map.get("extraContent");
            extraContent = extraContentList.toArray(new ItemStack[1]);
        }
    }

    public Kit(PlayerInventory playerInventory){
        this(playerInventory.getArmorContents(),playerInventory.getStorageContents(), playerInventory.getExtraContents());
    }

    public void giveToPlayer(Player player){
        player.getInventory().clear();
        player.getActivePotionEffects().clear();
        if(armorContent != null){
            player.getInventory().setArmorContents(armorContent);
        }
        if(storageContent != null){
            player.getInventory().setStorageContents(storageContent);
        }
        if(extraContent != null){
            player.getInventory().setExtraContents(extraContent);
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        if(displayName != null){
            map.put("displayName", displayName);
        }
        if(armorContent != null){
            map.put("armorContent", Arrays.stream(armorContent).toList());
        }
        if(storageContent != null){
            map.put("storageContent", Arrays.stream(storageContent).toList());
        }
        if(storageContent != null){
            map.put("extraContent", Arrays.stream(extraContent).toList());
        }
        return map;
    }
}
