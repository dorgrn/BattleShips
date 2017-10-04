package com.BattleShipsWebApp.mainGamesRoom.gameConfigsManager;

import BattleShipsEngine.engine.GameConfig;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class GameConfigsManager {
    private final Map<String, GameConfig> gameConfigs = new Hashtable<>(); // <game name, xml content>

    public void addConfig (final String name, final String filePath) {
        
    }

    public String removeConfig(final String name) {
        return null;
        //return gameConfigs.remove(name);
    }
//    public Map<String, String> getGameFiles() {
//        return Collections.unmodifiableMap(gameConfigs);
//    }

    public boolean isFileExists(final String name) {
        return gameConfigs.containsKey(name);
    }
}
