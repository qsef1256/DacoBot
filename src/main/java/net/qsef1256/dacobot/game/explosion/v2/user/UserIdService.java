package net.qsef1256.dacobot.game.explosion.v2.user;

import net.qsef1256.dacobot.game.explosion.domain.inventory.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UserIdService {

    private final UserService userService;

    public UserIdService(@NotNull UserService userService) {
        this.userService = userService;
    }

    public UserId getUserId(long discordId) {
        if (userService.isUserNotExist(discordId))
            throw new IllegalArgumentException("need user account for %s".formatted(discordId));

        return new UserId(userService.getUser(discordId));
    }

}
