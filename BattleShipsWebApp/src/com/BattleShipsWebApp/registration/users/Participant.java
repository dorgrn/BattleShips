package com.BattleShipsWebApp.registration.users;

import BattleShipsEngine.engine.Player;

public class Participant extends User {
    private Player.Type playerType;

    public Participant(String userName, Player.Type playerType) {
        super(userName);
        this.playerType = playerType;
    }

    public Player.Type getPlayerType() {
        return playerType;
    }
}
