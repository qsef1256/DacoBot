package net.qsef1256.dacobot.game.paint.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class PaintEntity {

    @Id
    private String paintName;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int xSize = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int ySize = 0;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PixelEntity> pixels;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Long createdUserId;

    private LocalDateTime createdTime = LocalDateTime.now();

}
