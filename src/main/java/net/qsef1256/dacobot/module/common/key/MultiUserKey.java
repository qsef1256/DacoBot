package net.qsef1256.dacobot.module.common.key;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dialib.util.CollectionUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;

@Slf4j
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
        Object[] users = this.users.stream().map(Objects::hashCode).toArray();

        int usersHashCode = 7079;
        for (Object user : users) {
            usersHashCode = usersHashCode ^ user.hashCode();
        }

        return Objects.hash(getType(), usersHashCode);
    }

    @Override
    public String toString() {
        StringBuilder userNames = new StringBuilder();
        for (User user : getUsers()) {
            userNames.append("[")
                    .append(user.getIdLong())
                    .append(", ")
                    .append(user.getName())
                    .append("]");
        }

        return "%s@%s [type = %s, users = %s]".formatted(getClass().getSimpleName(), hashCode(), getType(), userNames);
    }

}
