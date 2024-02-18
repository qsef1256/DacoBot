package net.qsef1256.dacobot.core.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OptionNotFoundException extends RuntimeException {

    private String optionName;

}
