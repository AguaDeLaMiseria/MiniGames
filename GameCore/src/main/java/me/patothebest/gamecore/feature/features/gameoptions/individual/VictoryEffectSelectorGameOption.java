package me.patothebest.gamecore.feature.features.gameoptions.individual;

import com.google.inject.Inject;
import me.patothebest.gamecore.cosmetics.projectiletrails.ProjectileManager;
import me.patothebest.gamecore.cosmetics.shop.ShopFactory;
import me.patothebest.gamecore.cosmetics.victoryeffects.VictoryManager;
import me.patothebest.gamecore.feature.AbstractFeature;
import me.patothebest.gamecore.feature.features.gameoptions.GameOption;
import me.patothebest.gamecore.gui.inventory.button.SimpleButton;
import me.patothebest.gamecore.itemstack.ItemStackBuilder;
import me.patothebest.gamecore.itemstack.Material;
import me.patothebest.gamecore.lang.CoreLang;
import org.bukkit.entity.Player;

public class VictoryEffectSelectorGameOption extends AbstractFeature implements GameOption {

    private final ShopFactory shopFactory;
    private final VictoryManager victoryManager;

    @Inject private VictoryEffectSelectorGameOption(ShopFactory shopFactory, VictoryManager victoryManager) {
        this.shopFactory = shopFactory;
        this.victoryManager = victoryManager;
    }

    @Override
    public SimpleButton getButton(Player player) {
        return new SimpleButton(new ItemStackBuilder().material(Material.FIREWORK_ROCKET).name(CoreLang.LOBBY_VICTORY_EFFECT_MENU.getMessage(player))).action(() -> {
            shopFactory.createShopMenu(player, victoryManager);
        });
    }
}
