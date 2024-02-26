package net.qsef1256.dacobot.module.account.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.game.explosion.domain.inventory.InventoryEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Nullable
    public UserEntity getUser(long discordId) {
        return userRepository.findByDiscordId(discordId).orElse(null);
    }

    @NotNull
    public UserEntity getUserOrCreate(long discordId) {
        return userRepository
                .findByDiscordId(discordId)
                .orElseGet(() -> createUser(discordId));
    }

    @NotNull
    public UserEntity createUser(long discordId) {
        UserEntity userData = new UserEntity();
        userData.setDiscordId(discordId);
        userData.setRegisterTime(LocalDateTime.now());
        userData.setInventory(new InventoryEntity()); // TODO: remove
        userData.setStatus("OK");

        return userRepository.save(userData);
    }

    public boolean isUserExist(long discordId) {
        return userRepository.existsById(discordId);
    }

    public boolean isUserNotExist(long discordId) {
        return !isUserExist(discordId);
    }

    public void deleteUser(long discordId) {
        userRepository.deleteById(discordId);
    }

}
