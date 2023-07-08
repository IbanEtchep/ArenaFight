package fr.iban.arenafight.task;

import fr.iban.arenafight.Fight;
import fr.iban.arenafight.interfaces.CallBack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartTask extends BukkitRunnable {

    private final Fight fight;
    private final CallBack callback;
    int i = 10;

    public StartTask(Fight fight, CallBack callBack) {
        this.fight = fight;
        this.callback = callBack;
    }

    @Override
    public void run() {
        if(i == 10 || i == 5 || i == 4 || i == 3 || i == 2 || i == 1){
            fight.getArenaPlayers().values().forEach(arenaPlayer -> {
                Player player = arenaPlayer.getPlayer();
                if(player != null){
                    player.showTitle(Title.title(
                            Component.text("Début du combat dans").color(TextColor.fromHexString("#7ed6df")),
                            Component.text(i).color(TextColor.fromHexString("#22a6b3")).append(Component.text(" secondes").color(TextColor.fromHexString("#7ed6df")))));
                    player.playNote(player.getLocation(), Instrument.STICKS, Note.flat(1, Note.Tone.A));
                }
            });
        }
        if(i == 0){
            fight.getArenaPlayers().values().forEach(arenaPlayer -> {
                Player player = arenaPlayer.getPlayer();
                if(player != null){
                    player.playNote(player.getLocation(), Instrument.BASS_DRUM, Note.flat(1, Note.Tone.A));
                    player.showTitle(Title.title(
                            Component.text("C'est parti !").color(TextColor.fromHexString("#eb4d4b")),
                            Component.text("Que le meilleur gagne !").color(TextColor.fromHexString("#ff7979"))));
                }
            });
            callback.call();
            cancel();
        }
        i--;
    }
}
