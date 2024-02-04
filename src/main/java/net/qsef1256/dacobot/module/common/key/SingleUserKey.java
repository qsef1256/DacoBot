package net.qsef1256.dacobot.module.common.key;

import lombok.Setter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.qsef1256.dacobot.core.jda.JdaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Set;

public class SingleUserKey extends ManagedKeyImpl implements UserKey {

    private final UserSnowflake user;
    @Setter(onMethod_ = {@Autowired})
    private JdaService jdaService;

    @Override
    public Set<User> getUsers() {
        return Set.of(jdaService.getUserFromId(user.getIdLong()));
    }

    public SingleUserKey(String type, UserSnowflake user) {
        super(type);
        this.user = user;
    }

    public User getUser() {
        return jdaService.getUserFromId(user.getIdLong());
    }

    public UserSnowflake getUserSnowflake() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SingleUserKey message)) return false;

        boolean userEqual = getUserSnowflake().equals(message.getUserSnowflake());
        boolean typeEqual = getType().equals(message.getType());
        return userEqual && typeEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), user.getIdLong());
    }

    @Override
    public String toString() {
        return "%s@%s [type = %s, user = %s]".formatted(getClass().getSimpleName(),
                hashCode(),
                getType(),
                user.getIdLong());
    }

}
