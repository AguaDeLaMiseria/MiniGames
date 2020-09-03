package me.patothebest.gamecore.feature.features.chests.refill;

import com.google.inject.Inject;
import me.patothebest.gamecore.event.arena.ArenaPrePhaseChangeEvent;
import me.patothebest.gamecore.event.player.ArenaLeaveEvent;
import me.patothebest.gamecore.feature.AbstractFeature;
import me.patothebest.gamecore.feature.features.gameoptions.GameOption;
import me.patothebest.gamecore.feature.features.gameoptions.GameOptionsGUIFactory;
import me.patothebest.gamecore.feature.features.gameoptions.weather.WeatherType;
import me.patothebest.gamecore.gui.inventory.button.SimpleButton;
import me.patothebest.gamecore.itemstack.ItemStackBuilder;
import me.patothebest.gamecore.itemstack.Material;
import me.patothebest.gamecore.lang.CoreLang;
import me.patothebest.gamecore.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class ChestTypeSelectorGameOption extends AbstractFeature implements GameOption {

    private final Map<Player, ChestType> chestTypeVoting = new HashMap<>();
    private final GameOptionsGUIFactory gameOptionsGUIFactory;

    @Inject
    private ChestTypeSelectorGameOption(GameOptionsGUIFactory gameOptionsGUIFactory) {
        this.gameOptionsGUIFactory = gameOptionsGUIFactory;
    }

    @Override
    public void initializeFeature() {
        super.initializeFeature();
        chestTypeVoting.clear();
    }

    @EventHandler
    public void onArenaLeave(ArenaLeaveEvent event) {
        chestTypeVoting.remove(event.getPlayer());
    }

    @EventHandler
    public void onArenaPhasePreChangeEvent(ArenaPrePhaseChangeEvent event) {
        if(event.getNewPhase() != arena.getNextPhase()) {
            return;
        }

        if(event.getArena() != arena) {
            return;
        }

        Map.Entry<ChestType, Integer> maxEntry = null;

        if(!chestTypeVoting.isEmpty()) {
            Map<ChestType, Integer> voteMapResults = new HashMap<>();

            for (Map.Entry<Player, ChestType> playerChestTypeEntry : chestTypeVoting.entrySet()) {
                voteMapResults.putIfAbsent(playerChestTypeEntry.getValue(), 0);
                Integer vote = voteMapResults.remove(playerChestTypeEntry.getValue());
                vote++;
                voteMapResults.put(playerChestTypeEntry.getValue(), vote);
            }

            for (Map.Entry<ChestType, Integer> entry : voteMapResults.entrySet()) {
                if(maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                    maxEntry = entry;
                }
            }


        }
    }

    @Override
    public SimpleButton getButton(Player player) {
        return new SimpleButton(new ItemStackBuilder().material(Material.ENDER_CHEST).name(player, CoreLang.GUI_CHEST_TYPE_ITEM)).action(() -> {
            if(!player.hasPermission(Permission.CHOOSE_CHESTTYPE.getBukkitPermission())) {
                CoreLang.GUI_CHEST_TYPE_NO_PERMISSION.sendMessage(player);
                return;
            }

            gameOptionsGUIFactory.openChestTypeUI(player, this);
        });
    }
    public Map<Player, ChestType> getChestTypeVoting() {
        return chestTypeVoting;
    }
}
