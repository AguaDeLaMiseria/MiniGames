package me.patothebest.gamecore.feature.features.chests.refill;

import me.patothebest.gamecore.lang.CoreLang;
import me.patothebest.gamecore.lang.interfaces.ILang;
import org.bukkit.Material;

public enum ChestType {

    BASIC(CoreLang.GUI_CHEST_TYPE_VOTE_BASIC,"basic", Material.DIRT, 2),
    NORMAL(CoreLang.GUI_CHEST_TYPE_VOTE_NORMAL,"normal", Material.IRON_INGOT, 4),
    OP(CoreLang.GUI_CHEST_TYPE_VOTE_OP, "op", Material.DIAMOND, 6)

    ;
    private final ILang langMessage;
    private final String chestType;
    private final int slot;
    private final Material displayMaterial;

    ChestType(ILang langMessage, String chestType, Material displayMaterial, int slot) {
        this.langMessage = langMessage;
        this.chestType = chestType;
        this.slot = slot;
        this.displayMaterial = displayMaterial;
    }

    public ILang getLangMessage() {
        return langMessage;
    }

    public Material getItem() {
        return displayMaterial;
    }

    public String getChestType() {
        return chestType;
    }

    public int getSlot(){
        return slot;
    }
}
