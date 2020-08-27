package me.patothebest.skywars.placeholder.placeholders.all;

import me.patothebest.gamecore.arena.AbstractArena;
import me.patothebest.gamecore.lang.CoreLang;
import me.patothebest.gamecore.lang.CoreLocaleManager;
import me.patothebest.gamecore.placeholder.PlaceHolder;
import me.patothebest.gamecore.player.IPlayer;
import me.patothebest.gamecore.player.PlayerManager;
import me.patothebest.skywars.lang.Lang;
import me.patothebest.skywars.phase.SkyWarsPhase;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class NextEventPlaceholder implements PlaceHolder {

    private final PlayerManager playerManager;

    @Inject public NextEventPlaceholder(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public String getPlaceholderName() {
        return "next_event";
    }

    @Override
    public String replace(Player player, String args) {
        return replace(playerManager.getPlayer(player).getCurrentArena());
    }

    @Override
    public String replace(AbstractArena arena) {
        return arena == null || arena.getPhase().getNextPhase() == null
                || !(arena.getPhase().getNextPhase() instanceof SkyWarsPhase) ? "None" : ((SkyWarsPhase)
                arena.getPhase().getNextPhase()).getPhaseType().getConfigName()
                .replace("null", "Ninguno")
                .replace("Refill", "Rellenado de cofres")
                .replace("Doom", "La muerte")
                .replace("Border Shrink", "Reducción del borde")
                .replace("End", "Final");
    }
}
