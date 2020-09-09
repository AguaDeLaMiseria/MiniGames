package me.patothebest.skywars.placeholder.placeholders.all;

import me.patothebest.gamecore.arena.AbstractArena;
import me.patothebest.gamecore.placeholder.PlaceHolder;
import me.patothebest.gamecore.player.PlayerManager;
import me.patothebest.skywars.arena.Arena;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class ChestTypePlaceHolder implements PlaceHolder {

    private final PlayerManager playerManager;

    @Inject
    public ChestTypePlaceHolder(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public String getPlaceholderName() {
        return "difficulty";
    }

    @Override
    public String replace(Player player, String args) {
        return replace(playerManager.getPlayer(player).getCurrentArena());
    }

    @Override
    public String replace(AbstractArena arena) {
        return arena == null ? "None" : ((Arena)arena).getChestType().getChestType().replace("basic", "Básico")
                .replace("normal", "Normal").replace("op", "OP");
    }
}