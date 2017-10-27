package com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager;

import BattleShipsEngine.engine.Game;
import com.BattleShipsWebApp.exceptions.GameRecordSizeException;
import com.BattleShipsWebApp.exceptions.RecordAlreadyExistsException;
import com.BattleShipsWebApp.registration.users.User;
import com.BattleShipsWebApp.utils.ServletUtils;
import com.BattleShipsWebApp.utils.SessionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GameRecordsManager {
    private final Set<GameRecord> gameRecords = new HashSet<>();

    public void addGameRecord(final String gameName, final String creatorName, Game game, int version)
            throws RecordAlreadyExistsException {
        GameRecord record = new GameRecord(gameName, creatorName, game, version);

        if (gameRecords.contains(record)) {
            throw new RecordAlreadyExistsException("in game records");
        }

        gameRecords.add(record);
    }

    public void addGameRecord(GameRecord record)
            throws RecordAlreadyExistsException {
        if (gameRecords.contains(record)) {
            throw new RecordAlreadyExistsException("in game records");
        }

        gameRecords.add(record);
    }

    public GameRecord getGameByName(String gameName) {
        Optional<GameRecord> result = gameRecords.stream()
                .filter(gameRecord -> gameRecord.getGameName().equals(gameName))
                .findFirst();

        return result.orElse(null);
    }

    public void removeGameRecord(final String gameName) throws GameRecordSizeException {
        GameRecord gameRecord = getGameByName(gameName);
        if (gameRecord != null) {
            gameRecords.remove(gameRecord);
        }
    }

    public Set<GameRecord> getGameRecords() {
        return Collections.unmodifiableSet(gameRecords);
    }

    public void addParticipantToGame(final User user, final GameRecord gameRecord) throws RecordAlreadyExistsException, GameRecordSizeException {
        gameRecord.addParticipant(user);
    }


    public void addWatcherToGame(final User user, final GameRecord gameRecord) throws RecordAlreadyExistsException, GameRecordSizeException {
        gameRecord.addWatcher(user);
    }

    public void resetGameRecord(String gameName, int newVersion) throws GameRecordSizeException {
        GameRecord gameRecord = getGameByName(gameName);
        if (gameRecord == null){
            throw new GameRecordSizeException("Game record doesn't exist");
        }
        gameRecord.resetGame(newVersion);
    }
}
