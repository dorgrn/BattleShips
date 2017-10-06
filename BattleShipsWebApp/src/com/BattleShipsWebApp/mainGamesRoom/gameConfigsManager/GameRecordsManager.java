package com.BattleShipsWebApp.mainGamesRoom.gameConfigsManager;

import BattleShipsEngine.engine.GameConfig;
import com.BattleShipsWebApp.exceptions.RecordAlreadyExistsException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GameRecordsManager {
    private final Set<GameRecord> gameRecords = new HashSet<>(); // <game name, xml content>

    public void addGameRecord(final String gameName, final String creatorName, GameConfig gameConfig)
            throws RecordAlreadyExistsException {
        GameRecord record = new GameRecord(gameName, creatorName, gameConfig);

        if (gameRecords.contains(record)){
            throw new RecordAlreadyExistsException("in game records");
        }

        gameRecords.add(record);
    }

    public void removeGameRecord(final String gameName) {
        Optional<GameRecord> gameConfig = gameRecords.stream()
                .filter(gameRecord -> gameRecord.getGameName().equals(gameName))
                .findFirst();
        gameConfig.ifPresent(gameRecords::remove);
    }
    public Set<GameRecord> getGameRecords() {
        return Collections.unmodifiableSet(gameRecords);
    }
}
