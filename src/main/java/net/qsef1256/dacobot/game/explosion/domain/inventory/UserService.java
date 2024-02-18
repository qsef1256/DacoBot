package net.qsef1256.dacobot.game.explosion.domain.inventory;


import net.qsef1256.dacobot.module.account.entity.UserEntity;
import net.qsef1256.dacobot.module.account.entity.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Nullable
    public UserEntity getUser(long discordId) {
        return userRepository.findByDiscordId(discordId).orElse(null);
    }

    @NotNull
    public UserEntity getUserOrCreate(long discordId) {
        return userRepository.findByDiscordId(discordId).orElseGet(() -> {
            UserEntity newUser = new UserEntity();
            newUser.setDiscordId(discordId);
            newUser.setInventory(new InventoryEntity());

            return userRepository.save(newUser);
        });
    }

}
