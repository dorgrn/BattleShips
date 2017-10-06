package com.BattleShipsWebApp.mainGamesRoom.gameConfigsManager;

import BattleShipsEngine.engine.GameConfig;
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
    private final int boardSize;

    public GameRecord(String gameName, String creatorName, GameConfig gameConfig) {
        this.gameName = gameName;
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
