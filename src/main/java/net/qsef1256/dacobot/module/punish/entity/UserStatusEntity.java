package net.qsef1256.dacobot.module.punish.entity;

import jakarta.persistence.*;
import lombok.Getter;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Entity
@Table(name = "user_status")
public class UserStatusEntity implements Serializable {

    @Id
    @OneToOne
    private UserEntity user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.NORMAL;
    @OneToOne
    @JoinColumn(name = "primary_punish")
    private PunishEntity primaryPunish;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> oEffectiveClass = o instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass() :
                getClass();

        if (thisEffectiveClass != oEffectiveClass) return false;

        UserStatusEntity that = (UserStatusEntity) o;
        return getUser() != null && Objects.equals(getUser(), that.getUser());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy hibernateProxy
                ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

}
