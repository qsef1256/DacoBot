package net.qsef1256.dacobot.game.paint.repository;

import net.qsef1256.dacobot.game.paint.entity.PaintEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaintRepository extends JpaRepository<PaintEntity, String> {

    @NotNull List<PaintEntity> findByOwnerId(long ownerId);

}