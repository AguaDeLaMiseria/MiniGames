package me.patothebest.gamecore.treasure.chest;

import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import me.patothebest.gamecore.hologram.Hologram;
import me.patothebest.gamecore.nms.NMS;
import me.patothebest.gamecore.player.IPlayer;
import me.patothebest.gamecore.title.Title;
import me.patothebest.gamecore.title.TitleManager;
import me.patothebest.gamecore.treasure.TreasureFactory;
import me.patothebest.gamecore.treasure.type.TreasureType;
import me.patothebest.gamecore.util.SerializableObject;
import me.patothebest.gamecore.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.material.EnderChest;

import java.util.Map;

public class TreasureChestLocation implements SerializableObject {

    private final Provider<NMS> nmsProvider;
    private final Location location;
    private final EnderChest chest;
    private final TreasureFactory treasureFactory;
    private Hologram hologram;
    private Hologram hologram2;
    private Hologram hologram3;
    private TreasureChest currentTreasureChest;

    @AssistedInject private TreasureChestLocation(Provider<NMS> nmsProvider, TreasureFactory treasureFactory, @Assisted Location location) {
        this.nmsProvider = nmsProvider;
        this.location = location;
        this.location.getBlock().getState().setType(Material.ENDER_CHEST);
        this.treasureFactory = treasureFactory;

        chest = (EnderChest) location.getBlock().getState().getData();
        createHologram();
    }

    @SuppressWarnings("unchecked")
    @AssistedInject private TreasureChestLocation(Provider<NMS> nmsProvider, TreasureFactory treasureFactory, @Assisted Map<String, Object> data) {
        this.nmsProvider = nmsProvider;
        this.location = Location.deserialize((Map<String, Object>) data.get("location"));
        this.location.getBlock().getState().setType(Material.ENDER_CHEST);
        this.treasureFactory = treasureFactory;

        chest = (EnderChest) location.getBlock().getState().getData();

        createHologram();
    }

    public boolean openChest(IPlayer player, TreasureType treasureType) {
        if(currentTreasureChest != null) {
            return false;
        }

        currentTreasureChest = treasureFactory.createChest(player, treasureType, this, (location) -> location.currentTreasureChest = null);
        currentTreasureChest.playAnimation(location.clone().add(-3, 3, -3));

        player.getPlayer().teleport(location.clone().add(0.5, 1, 0.5));
        Title title = TitleManager.newInstance(ChatColor.GREEN + "Cofre " + treasureType.getName());
        title.setFadeInTime(1);
        title.setStayTime(3);
        title.setFadeOutTime(1);
        title.send(player.getPlayer());
        return true;
    }

    public void createHologram() {
        hologram = nmsProvider.get().createHologram(location.clone().add(0.5, 1.30, 0.5));
        hologram2 = nmsProvider.get().createHologram(location.clone().add(0.5, 1, 0.5));
        hologram3 = nmsProvider.get().createHologram(location.clone().add(0.5, 0.70, 0.5));
        hologram.setName(Utils.format("&6&lCofre de Tesoros"));
        hologram2.setName(Utils.format("&bAquí puedes &dcomprar cofres &bcon recompensas"));
        hologram3.setName(Utils.format("&bcomo &dkits, jaulas, efectos, dinero, &by más!"));
    }

    /**
     * Gets the location
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the bukkit chest
     *
     * @return the chest
     */
    public EnderChest getChest() {
        return chest;
    }

    /**
     * Gets hologram
     *
     * @return the hologram
     */
    public Hologram getHologram() {
        return hologram;
    }

    public Hologram getHologram2(){
        return hologram2;
    }

    public Hologram getHologram3(){
        return hologram3;
    }

    /**
     * @return true if another player is not opening the chest
     */
    public boolean canOpen() {
        return currentTreasureChest == null;
    }

    /**
     * @return true if another player is opening the chest
     */
    public boolean isNotAvailable() {
        return currentTreasureChest != null;
    }

    /**
     * @return the current treasure chest
     */
    public TreasureChest getCurrentTreasureChest() {
        return currentTreasureChest;
    }

    /**
     * Creates a Map representation of this class.
     * <p>
     * This class must provide a method to restore this class, as defined in
     * the {@link ConfigurationSerializable} interface javadocs.
     *
     */
    @Override
    public void serialize(Map<String, Object> data) {
        data.put("location", location.serialize());
    }
}