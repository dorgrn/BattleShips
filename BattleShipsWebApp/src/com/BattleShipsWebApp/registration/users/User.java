package com.BattleShipsWebApp.registration.users;

import BattleShipsEngine.engine.Player;

public class User {
    private final String username;
    private final Player.Type playerType; // player order in logic: PLAYER_ONE (creator) / PLAYER_TWO (joins)

    public String getUserName() {
        return username;
    }
    //private final String sessionID;

    public User(String userName, Player.Type playerType) {
        this.username = userName;
        this.playerType = playerType;
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
