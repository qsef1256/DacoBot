package net.qsef1256.dacobot.game.paint.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "pixel_entity")
public class PixelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pixelId;

    private int x;
    private int y;

    @Enumerated(EnumType.STRING)
    private ColorEmoji pixelColor;

}
