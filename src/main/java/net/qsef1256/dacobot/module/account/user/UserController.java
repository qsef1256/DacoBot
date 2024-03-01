package net.qsef1256.dacobot.module.account.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashService;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CashService cashService;
    private final JdaService jdaService;

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discordId User's snowflake id
     */
    public void register(long discordId) {
        try {
            if (userService.isUserExist(discordId))
                throw new DacoAccountException("%s 유저는 이미 등록 되어 있습니다."
                        .formatted(jdaService.getNameAsTag(discordId)));

            userService.createUser(discordId);
        } catch (DacoAccountException e) {
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new DacoAccountException("%s 유저 등록에 실패했습니다"
                    .formatted(jdaService.getNameAsTag(discordId)));
        }
    }

    /**
     * 계정을 삭제합니다.
     *
     * @param discordId User's snowflake id
     */
    @Transactional
    public void delete(long discordId) {
        try {
            if (userService.isUserNotExist(discordId))
                throw new DacoAccountException("%s 계정은 이미 삭제 되었습니다."
                        .formatted(jdaService.getNameAsTag(discordId)));

            cashService.deleteCash(discordId);
            userService.deleteUser(discordId);
        } catch (DacoAccountException e) {
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage());

            throw new DacoAccountException("%s 계정 삭제에 실패했습니다."
                    .formatted(jdaService.getNameAsTag(discordId)));
        }
    }

    public void reset(long discordId) {
        // TODO: implement this
    }

    @NotNull
    public UserEntity getUser(long discordId) {
        return Optional
                .ofNullable(userService.getUser(discordId))
                .orElseThrow(() -> new DacoAccountException("%s 유저는 등록되지 않았습니다."
                        .formatted(jdaService.getNameAsTag(discordId))));
    }

}
