package net.qsef1256.dacobot.game.explosion.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashRepository extends JpaRepository<CashEntity, Long> {

    Optional<CashEntity> findByDiscordUser_DiscordId(long discordId);

}
