package net.qsef1256.dacobot.service.key;

import lombok.Getter;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.util.CollectionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class MultiUserKey extends ManagedKeyImpl implements UserKey {

    @Getter
    private final Set<User> users;

    public MultiUserKey(@NotNull String type, Set<User> users) {
        super(type);
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MultiUserKey message)) return false;

        boolean typeEqual = getType().equals(message.getType());
        boolean userEqual = CollectionUtil.isSame(getUsers(), message.getUsers());

        return userEqual && typeEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), Arrays.hashCode(users.stream().map(ISnowflake::getIdLong).toArray()));
    }

    @Override
    public String toString() {
        StringBuilder userNames = new StringBuilder();
        for (User user : getUsers()) {
            userNames.append(user.getAsTag()).append(", ");
        }

        return "%s@%s [type = %s, users = %s".formatted(getClass().getSimpleName(), hashCode(), getType(), userNames);
    }

}
