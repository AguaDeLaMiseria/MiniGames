package me.patothebest.gamecore.feature.features.chests.refill;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.patothebest.gamecore.CorePlugin;
import me.patothebest.gamecore.gui.inventory.GUIPage;
import me.patothebest.gamecore.gui.inventory.button.SimpleButton;
import me.patothebest.gamecore.itemstack.ItemStackBuilder;
import me.patothebest.gamecore.lang.CoreLang;
import org.bukkit.entity.Player;

public class ChestTypeUI extends GUIPage {

    private final ChestTypeSelectorGameOption chestTypeSelectorGameOption;

    @Inject
    private ChestTypeUI(CorePlugin plugin, @Assisted Player player, @Assisted ChestTypeSelectorGameOption chestTypeSelectorGameOption) {
        super(plugin, player, CoreLang.GUI_CHEST_TYPE_VOTE_TITLE.getMessage(player), 9);
        this.chestTypeSelectorGameOption = chestTypeSelectorGameOption;
        build();
    }

    @Override
    protected void buildPage() {
        ChestType selectedChestType = chestTypeSelectorGameOption.getChestTypeVoting().get(getPlayer());

        for (ChestType chestType : ChestType.values()) {
            addButton(new SimpleButton(new ItemStackBuilder(chestType.getItem())
                    .name(chestType.getLangMessage().getMessage(getPlayer()))
                    .lore(selectedChestType == chestType ? CoreLang.GUI_SHOP_SELECTED.getMessage(getPlayer()) : CoreLang.GUI_SHOP_CLICK_TO_SELECT.getMessage(getPlayer()))
                    .glowing(selectedChestType == chestType))
                    .action(() -> {
                        chestTypeSelectorGameOption.getChestTypeVoting().remove(player);

                        CoreLang.GUI_CHEST_TYPE_VOTE_VOTED.replaceAndSend(player, chestType.getLangMessage());
                        chestTypeSelectorGameOption.getChestTypeVoting().put(player, chestType);
                        refresh();
                    }), chestType.getSlot());
        }
    }
}
