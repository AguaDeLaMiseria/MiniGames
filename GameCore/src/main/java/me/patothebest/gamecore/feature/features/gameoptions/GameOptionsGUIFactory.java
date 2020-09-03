package me.patothebest.gamecore.feature.features.gameoptions;

import me.patothebest.gamecore.feature.features.chests.refill.ChestRefillFeature;
import me.patothebest.gamecore.feature.features.chests.refill.ChestTypeSelectorGameOption;
import me.patothebest.gamecore.feature.features.chests.refill.ChestTypeUI;
import me.patothebest.gamecore.feature.features.gameoptions.time.TimeSelectorGameOption;
import me.patothebest.gamecore.feature.features.gameoptions.time.TimeVoteUI;
import me.patothebest.gamecore.feature.features.gameoptions.weather.WeatherSelectorGameOption;
import me.patothebest.gamecore.feature.features.gameoptions.weather.WeatherVoteUI;
import org.bukkit.entity.Player;

public interface GameOptionsGUIFactory {

    WeatherVoteUI openWeatherUI(Player player, WeatherSelectorGameOption weatherSelectorGameOption);

    TimeVoteUI openTimeUI(Player player, TimeSelectorGameOption timeSelectorGameOption);

    ChestTypeUI openChestTypeUI(Player player, ChestTypeSelectorGameOption chestTypeSelectorGameOption);

}
