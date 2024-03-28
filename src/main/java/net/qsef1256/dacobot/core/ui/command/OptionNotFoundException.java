package net.qsef1256.dacobot.core.ui.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionNotFoundException extends RuntimeException {

    private final String optionName;

}
