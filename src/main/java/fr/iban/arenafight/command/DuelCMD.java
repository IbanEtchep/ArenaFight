package fr.iban.arenafight.command;

import fr.iban.arenafight.ArenaFightPlugin;
import fr.iban.arenafight.DuelRequest;
import fr.iban.arenafight.Fight;
import fr.iban.arenafight.menu.KitSelectMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DuelCMD implements CommandExecutor {

    private final ArenaFightPlugin plugin;
    private final Map<UUID, DuelRequest> duelsRequests = new HashMap<>();

    public DuelCMD(ArenaFightPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            if(args.length == 0){
                player.sendMessage("/duel <joueur>");
            }
            if(args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null && target != player){
                    new KitSelectMenu(player, kit -> {
                        duelsRequests.put(player.getUniqueId(), new DuelRequest(player, target, kit));
                        String bar = "§7§m---------------------------------------------";
                        Component accept = Component.text("ACCEPTER")
                                .color(TextColor.fromCSSHexString("#6ab04c"))
                                .clickEvent(ClickEvent.runCommand("/duel accept " + player.getName()))
                                .hoverEvent(HoverEvent.showText(Component.text("Cliquez pour accepter la demande").color(TextColor.fromHexString("#badc58"))));

                        Component deny = Component.text("REFUSER")
                                .color(TextColor.fromCSSHexString("#eb4d4b"))
                                .clickEvent(ClickEvent.runCommand("/duel deny " + player.getName()))
                                .hoverEvent(HoverEvent.showText(Component.text("Cliquez pour refuser la demande").color(TextColor.fromHexString("#ff7979"))));

                        target.sendMessage(bar);
                        target.sendMessage("§6"+ player.getName() + "§f vous défie en §eduel§f avec le kit §e"+ kit.getDisplayName() +"§f !");
                        target.sendMessage(Component.text("Vous pouvez ").append(accept).append(Component.text(" ou ")).append(deny).append(Component.text(".")));
                        target.sendMessage(bar);
                        player.sendMessage("§aLa demande a bien été envoyée.");
                    }).open();
                }else{
                    player.sendMessage("§cCe joueur n'est pas en ligne.");
                }
            }
            if(args.length == 2){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    if(duelsRequests.containsKey(target.getUniqueId()) && duelsRequests.get(target.getUniqueId()).getPlayerTo() == player){
                        switch (args[0]) {
                            case "accept" -> {
                                target.sendMessage("§a" + player.getName() + " a accepté votre demande de duel. Vous avez été placés dans la file d'attente.");
                                player.sendMessage("§aVotre duel a été placé dans la file d'attente.");
                                plugin.getFightManager().addDuelToQueue(duelsRequests.get(target.getUniqueId()));
                                duelsRequests.remove(target.getUniqueId());
                            }
                            case "deny" -> {
                                target.sendMessage("§c" + player.getName() + " a rejeté votre demande de duel.");
                                player.sendMessage("§cVous avez rejeté la demande de duel.");
                                duelsRequests.remove(target.getUniqueId());
                            }
                        }
                    }else{
                        player.sendMessage("§cVous n'avez pas de requête de ce joueur.");
                    }
                }else{
                    player.sendMessage("§cLe joueur n'est plus en ligne.");
                }
            }
        }
        return false;
    }
}
