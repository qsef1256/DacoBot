package net.qsef1256.dacobot.game.explosion.v2.cash;

import net.qsef1256.dacobot.game.explosion.v2.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashRepository extends JpaRepository<CashEntity, UserId> {

}