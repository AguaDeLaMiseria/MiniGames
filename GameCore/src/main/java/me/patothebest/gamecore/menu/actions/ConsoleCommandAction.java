package me.patothebest.gamecore.menu.actions;

import me.patothebest.gamecore.menu.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class ConsoleCommandAction implements Action {

    private String console;

    @Override
    public void load(Map<String, Object> map) {
        console = (String) map.get("console");
    }

    @Override
    public void execute(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), console.replace("%player_name%", player.getName()));
    }
}