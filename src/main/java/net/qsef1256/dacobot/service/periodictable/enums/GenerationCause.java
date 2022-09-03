package net.qsef1256.dacobot.service.periodictable.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.qsef1256.dacobot.util.EnumUtil;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum GenerationCause {

    NATURAL("천연 원소"),
    DECAY("붕괴로 생성"),
    ARTIFICIAL("인공 원소");

    private static final Map<String, GenerationCause> BY_NAME = EnumUtil.toMap(GenerationCause::getName, GenerationCause.class);

    private final String name;

    public static GenerationCause getByName(String name) {
        return BY_NAME.get(name);
    }

}
