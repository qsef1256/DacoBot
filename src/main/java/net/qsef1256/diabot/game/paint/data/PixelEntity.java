package net.qsef1256.diabot.game.paint.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.diabot.game.paint.enums.PixelColor;

import javax.persistence.*;

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
    private PixelColor pixelColor;

}
