package net.qsef1256.dacobot.module.punish.entity;

import lombok.Getter;
import net.qsef1256.dialib.util.EnumUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
public enum PunishType {

    WARN("경고", "경고 처벌, 처벌 목록에 사유가 기록됩니다."),
    BAN("밴", "밴 처벌, 모든 명령어를 사용할 수 없게 합니다.");

    private static final Map<String, PunishType> fromName = EnumUtil.toMap(PunishType::getName, PunishType.class);

    private final String name;
    private final String desc;

    PunishType(@NotNull String name, @NotNull String desc) {
        this.name = name;
        this.desc = desc;
    }

    @Nullable
    public static PunishType fromName(@NotNull String name) {
        return fromName.get(name);
    }

}
