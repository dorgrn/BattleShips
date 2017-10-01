package com.BattleShipsWebApp.mainGamesRoom.gameFilesManager;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class GameFilesManager {
    private final Map<String,String> gameFiles = new Hashtable<>(); // <game name, xml content>

    public void addFile(final String name, final String content) {
        gameFiles.put(name,content);
    }

    public String removeFile(final String name) {
        return gameFiles.remove(name);
    }

    public Map<String, String> getGameFiles(){
        return Collections.unmodifiableMap(gameFiles);
    }

    public boolean isFileExists(final String name) {
        return gameFiles.containsKey(name);
    }
}
