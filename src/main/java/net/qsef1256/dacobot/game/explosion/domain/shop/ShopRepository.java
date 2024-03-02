package net.qsef1256.dacobot.game.explosion.domain.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long> {

    ShopEntity findFirstBy();
}
