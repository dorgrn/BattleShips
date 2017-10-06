package com.BattleShipsWebApp.registration.users;

public class User {
    private final String username;

    public String getUserName() {
        return username;
    }
    //private final String sessionID;

    public User(String userName) {
        this.username = userName;
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
