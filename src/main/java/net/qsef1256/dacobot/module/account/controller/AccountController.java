package net.qsef1256.dacobot.module.account.controller;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import net.qsef1256.dacobot.module.account.entity.UserRepository;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AccountController {

    private final UserRepository userRepository;
    private final JdaService jdaService;

    public AccountController(@NotNull UserRepository userRepository,
                             @NotNull JdaService jdaService) {
        this.userRepository = userRepository;
        this.jdaService = jdaService;
    }

    /**
     * 다양한 기능을 사용하기 위해 유저 등록을 시도합니다.
     *
     * @param discordId User's snowflake id
     */
    public void register(long discordId) {
        try {
            if (userRepository.existsById(discordId))
                throw new DacoAccountException(jdaService.getNameAsTag(discordId) + " 유저는 이미 등록 되어 있습니다.");

            UserEntity userData = new UserEntity();
            userData.setDiscordId(discordId);
            userData.setRegisterTime(LocalDateTime.now());
            userData.setStatus("OK");

            userRepository.saveAndFlush(userData);
        } catch (DacoAccountException e) {
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new DacoAccountException(jdaService.getNameAsTag(discordId) + " 유저 등록에 실패했습니다");
        }
    }

    /**
     * 계정을 삭제합니다.
     *
     * @param discordId User's snowflake id
     */
    public void delete(long discordId) {
        try {
            if (!userRepository.existsById(discordId))
                throw new DacoAccountException(jdaService.getNameAsTag(discordId) + " 계정은 이미 삭제 되었습니다.");
            userRepository.deleteById(discordId);
        } catch (DacoAccountException e) {
            throw e;
        } catch (RuntimeException e) {
            log.error(e.getMessage());

            throw new DacoAccountException(jdaService.getNameAsTag(discordId) + " 계정 삭제에 실패했습니다.");
        }
    }

    @NotNull
    public UserEntity getAccount(long discordId) {
        return userRepository
                .findById(discordId)
                .orElseThrow(() -> new DacoAccountException(jdaService.getNameAsTag(discordId) + " 유저는 등록되지 않았습니다."));
    }

    public void save(@NotNull UserEntity userData) {
        userRepository.saveAndFlush(userData);
    }

}
