package com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager;

import BattleShipsEngine.engine.Game;
import BattleShipsEngine.engine.GameConfig;
import com.BattleShipsWebApp.exceptions.RecordAlreadyExistsException;
import com.BattleShipsWebApp.exceptions.RecordDoesnNotExistsException;
import com.BattleShipsWebApp.registration.users.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameRecord {
    private final String gameName;
    private final User creator;
    private final GameStatus gameStatus = GameStatus.ONE_PLAYER;
    private transient final GameConfig gameConfig; // transient = not for serialization
    private final Set<User> participants;
    private final Set<User> watchers;
    private final Game game;
    private final int boardSize;

    public GameRecord(String gameName, String creatorName, Game game, GameConfig gameConfig) {
        this.gameName = gameName;
        this.game = game;
        this.creator = new User(creatorName);
        this.gameConfig = gameConfig;
        this.participants = new HashSet<>();
        participants.add(creator);
        this.watchers = new HashSet<>();
        this.boardSize = gameConfig.getGame().getBoardSize();
    }

    public String getGameName() {
        return gameName;
    }

    public User getCreator() {
        return creator;
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public Set<User> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public Set<User> getWatchers() {
        return Collections.unmodifiableSet(watchers);
    }

    public void addParticipant(User user) throws RecordAlreadyExistsException {
        if (participants.contains(user)){
            throw new RecordAlreadyExistsException("User is already a partcipant! " + user.getUserName());
        }

        participants.add(user);
    }

    public void addWatcher(User user) throws RecordAlreadyExistsException {
        if (watchers.contains(user)){
            throw new RecordAlreadyExistsException("User is already a watcher! " + user.getUserName());
        }

        watchers.add(user);
    }

    public void removeParticipant(User user) throws RecordDoesnNotExistsException {
        if (!participants.contains(user)){
            throw new RecordDoesnNotExistsException("User is not a participant! " + user.getUserName());
        }

        participants.remove(user);
    }

    public void removeWatcher(User user) throws RecordDoesnNotExistsException {
        if (!watchers.contains(user)){
            throw new RecordDoesnNotExistsException("User is not a watcher! " + user.getUserName());
        }

        watchers.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameRecord)) return false;

        GameRecord that = (GameRecord) o;

        return gameName.equals(that.gameName);
    }

    @Override
    public int hashCode() {
        return gameName.hashCode();
    }
}
