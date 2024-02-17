package net.qsef1256.dacobot.game.explosion.v2.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Component
public class UserId implements Serializable {

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
