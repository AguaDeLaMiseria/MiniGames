package me.patothebest.gamecore.feature.features.chests.refill;

import com.google.inject.Inject;
import me.patothebest.gamecore.CorePlugin;
import me.patothebest.gamecore.event.arena.ArenaPrePhaseChangeEvent;
import me.patothebest.gamecore.event.player.ArenaLeaveEvent;
import me.patothebest.gamecore.feature.features.gameoptions.GameOption;
import me.patothebest.gamecore.feature.features.gameoptions.GameOptionsGUIFactory;
import me.patothebest.gamecore.file.CoreConfig;
import me.patothebest.gamecore.gui.inventory.button.SimpleButton;
import me.patothebest.gamecore.itemstack.ItemStackBuilder;
import me.patothebest.gamecore.itemstack.Material;
import me.patothebest.gamecore.lang.CoreLang;
import me.patothebest.gamecore.modules.Module;
import me.patothebest.gamecore.arena.types.ChestArena;
import me.patothebest.gamecore.feature.AbstractFeature;
import me.patothebest.gamecore.permission.Permission;
import me.patothebest.gamecore.phase.phases.TeamAssignPhase;
import me.patothebest.gamecore.vector.ArenaLocation;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChestRefillFeature extends AbstractFeature implements Module, GameOption {

    private final static Map<ChestLocation, Map<ChestType, ChestFile>> CHEST_FILES = new HashMap<>();

    private final Map<Player, ChestType> chestTypeVoting = new HashMap<>();
    private final GameOptionsGUIFactory gameOptionsGUIFactory;

    private final Map<ChestLocation, List<ArenaLocation>> arenaChestLocations = new HashMap<>();
    private final Set<ChestLocation> chestLocations;
    private int minAmount = -1;
    private int maxAmount = -1;
    private ChestArena chestArena;

    @Inject private ChestRefillFeature(CorePlugin plugin, CoreConfig coreConfig, Set<ChestLocation> chestLocations, GameOptionsGUIFactory gameOptionsGUIFactory) {
        this.chestLocations = chestLocations;
        this.gameOptionsGUIFactory = gameOptionsGUIFactory;
        if (coreConfig.isSet("chests")) {
            minAmount = coreConfig.getInt("chests.min-items-amount", -1);
            maxAmount = coreConfig.getInt("chests.max-items-amount", -1);
        }

        if (CHEST_FILES.isEmpty()) {
            for (ChestLocation chestLocation : chestLocations) {
                Map<ChestType, ChestFile> chestMap = new HashMap<>();
                for (ChestType chestType : ChestType.values()) {
                    chestMap.put(chestType, new ChestFile(plugin, chestLocation, chestType));
                }

                CHEST_FILES.put(chestLocation, chestMap);
            }
        }
    }

    @Override
    public void initializeFeature() {
        if (!(arena instanceof ChestArena)) {
            throw new IllegalStateException(arena.getName() + " must implement the interface ChestArena!");
        }

        super.initializeFeature();

        arenaChestLocations.clear();
        chestTypeVoting.clear();
        this.chestArena = (ChestArena) arena;


        for (ChestLocation chestLocation : chestLocations) {
            arenaChestLocations.put(chestLocation, ((ChestArena) arena).getArenaChests(chestLocation));
        }
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

        if (!(event.getNewPhase() instanceof TeamAssignPhase)) {
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
        chestArena.setChestType(maxEntry != null ? maxEntry.getKey() : ChestType.NORMAL);
        refill();
        arena.sendTitleToArena((player, titleBuilder) ->
                titleBuilder.withFadeInTime(1)
                        .withStayTime(2)
                        .withFadeOutTime(1)
                        .withTitle(CoreLang.DIFFICULTY.replace(player, chestArena.getChestType().getChestType()
                                .replace("basic", "Básico")
                                .replace("normal", "Normal").replace("op", "OP"))));
    }

    @Override
    public void stopFeature() {
        super.stopFeature();
        arenaChestLocations.clear();
    }


    public void refill() {
        arenaChestLocations.forEach((chestLocation, arenaLocations) -> {
            arenaLocations.forEach(location -> {
                if (location.getBlock().getType() != Material.CHEST.parseMaterial() && location.getBlock().getType() != Material.TRAPPED_CHEST.parseMaterial()) {
                    return;
                }

                Chest chest = (Chest) location.getBlock().getState();
                Inventory inventory = chest.getBlockInventory();
                ChestFile chestFile = CHEST_FILES.get(chestLocation).get(chestArena.getChestType());
                inventory.clear();
                chestFile.fill(inventory, minAmount, maxAmount);
            });
        });
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