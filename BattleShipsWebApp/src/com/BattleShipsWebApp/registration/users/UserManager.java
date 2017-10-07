package com.BattleShipsWebApp.registration.users;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserManager {
    private final Set<User> usersSet;

    public UserManager() {
        this.usersSet = new HashSet<>();
    }

    public void addUser(String username) {
        usersSet.add(new User(username));
    }

    public void removeUser(final String username ){
        Optional<User> optionalUser = usersSet.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst();

        optionalUser.ifPresent(usersSet::remove);
    }

    public void removeUser(User user) {
        usersSet.remove(user);
    }

    public Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(User user) {
        return usersSet.contains(user);
    }

    public User getUser(String username) {
        Optional<User> result = usersSet.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst();

        return result.orElse(null);
    }
}
