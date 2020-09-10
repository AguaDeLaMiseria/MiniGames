package me.patothebest.gamecore.cosmetics.shop;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class AbstractShopItem implements ShopItem {

    private final String configName;
    private final String displayName;
    private final List<String> description;
    private final int price;
    private final String permission;
    private final boolean permanent;
    private final String command;
    private final String message;

    public AbstractShopItem() {
        configName = "";
        displayName = "";
        description = Collections.emptyList();
        price = 0;
        permission = "";
        permanent = true;
        command = "";
        message = "";
    }

    @SuppressWarnings("unchecked")
    public AbstractShopItem(String configName, Map<String, Object> data) {
        this.configName = configName;
        this.displayName = (String) data.get("name");
        this.description = (List<String>) data.get("description");
        this.price = (int) data.getOrDefault("price", 0);
        this.permission = (String) data.getOrDefault("permission", "");
        this.permanent = (boolean) data.getOrDefault("permanent", true);
        this.command = (String) data.get("command");
        this.message = (String) data.get("message");
    }

    @Override
    public final List<String> getDescription() {
        return description;
    }

    @Override
    public final int getPrice() {
        return price;
    }

    @Override
    public final String getPermission() {
        return permission;
    }

    @Override
    public final String getDisplayName() {
        return displayName;
    }

    @Override
    public final String getName() {
        return configName;
    }

    @Override
    public final boolean isPermanent() {
        return permanent;
    }

    @Override
    public final String getCommand() {return command;}

    @Override
    public final String getMessage() {return message;}
}
