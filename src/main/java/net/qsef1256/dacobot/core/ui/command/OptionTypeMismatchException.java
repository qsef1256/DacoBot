package net.qsef1256.dacobot.core.ui.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionTypeMismatchException extends RuntimeException {

    private final String optionName;
    private final String optionValue;
    private final String targetType;

}
