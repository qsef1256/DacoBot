package net.qsef1256.dacobot.service.key;

import lombok.Getter;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;
import java.util.Set;

public class SingleUserKey extends ManagedKeyImpl implements UserKey {

    @Getter
    private final User user;

    @Override
    public Set<User> getUsers() {
        return Set.of(user);
    }

    public SingleUserKey(String type, User user) {
        super(type);
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SingleUserKey message)) return false;

        boolean userEqual = getUser().getIdLong() == message.getUser().getIdLong();
        boolean typeEqual = getType().equals(message.getType());
        return userEqual && typeEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), user.getIdLong());
    }

    @Override
    public String toString() {
        return "%s@%s [type = %s, user = %s]".formatted(getClass().getSimpleName(), hashCode(), getType(), user.getAsTag());
    }

}
