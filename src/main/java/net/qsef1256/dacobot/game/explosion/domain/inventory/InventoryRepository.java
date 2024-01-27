package net.qsef1256.dacobot.game.explosion.domain.inventory;

import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findByDiscordUser(UserEntity discordUser);

}
