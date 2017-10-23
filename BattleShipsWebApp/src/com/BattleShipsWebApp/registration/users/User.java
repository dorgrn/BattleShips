package com.BattleShipsWebApp.registration.users;

import com.BattleShipsWebApp.exceptions.GameRecordSizeException;
import com.BattleShipsWebApp.mainGamesRoom.gameRecordsManager.GameRecord;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final String username;
    private final Set<GameRecord> participantInGames = new HashSet<>();

    public String getUserName() {
        return username;
    }
    //private final String sessionID;


    public User(String userName) {
        this.username = userName;
    }

    public void addToUserGameParticpantList(final GameRecord gameRecord) throws GameRecordSizeException {
        if (participantInGames.contains(gameRecord)){
            throw new GameRecordSizeException("User " + username +  " is already a partcipant in game "+ gameRecord.getGameName());
        }

        participantInGames.add(gameRecord);
    }

    public void removeFromUserGameParticpantList(final GameRecord gameRecord) throws GameRecordSizeException {
        if (!participantInGames.contains(gameRecord)){
            throw new GameRecordSizeException("User " + username +  " isn't a partcipant in game "+ gameRecord.getGameName());
        }

        participantInGames.remove(gameRecord);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
