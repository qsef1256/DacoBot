package net.qsef1256.dacobot.core.ui.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.commandclient.CommandClientService;
import net.qsef1256.dacobot.module.cmdstat.CmdStatisticEntity;
import net.qsef1256.dacobot.module.cmdstat.CmdStatisticService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Extended version of {@link SlashCommand}, containing getOption, statistic service
 */
@Component
public abstract class DacoCommand extends SlashCommand {

    @Nullable
    private SlashCommandEvent event = null;

    @Getter
    private CmdStatisticEntity cmdStatistic;
    protected boolean statistic = false;

    private CmdStatisticService statisticService;
    private CommandClientService commandClientService;

    @Override
    protected final void execute(@NotNull SlashCommandEvent event) {
        this.event = event;
        if (statistic)
            cmdStatistic = statisticService.addCmdStatistic(getClass().getSimpleName());

        runCommand(event);
    }

    protected abstract void runCommand(@NotNull SlashCommandEvent event);

    @Autowired
    public void setStatisticService(@NotNull CmdStatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @Autowired
    public void setCommandClientService(@NotNull CommandClientService commandClientService) {
        this.commandClientService = commandClientService;
    }

    /**
     * Get command statistic like "금일: 4 총합: 46"
     * <p>Required {@code statistic = true} in constructor.
     *
     * @return use info
     */
    protected String getUseInfo() {
        if (!statistic)
            throw new IllegalStateException("statistic is disabled, can't command use statistic info.");
        CmdStatisticEntity statistic = getCmdStatistic();

        return "금일: %d 총합: %d".formatted(statistic.getTodayUsed(), statistic.getUseCount());
    }

    /**
     * Reply to a message informing you that additional commands are needed.
     *
     * @param member executing member
     * @return message like 추가 명령어를 입력하세요! : 추가, 삭제
     */
    @NotNull
    protected String needSubCommand(@Nullable Member member) {
        String[] childNames = new String[children.length];

        int i = 0;
        for (Command child : children) {
            if (commandClientService.canExecute(child, member))
                childNames[i] = child.getName();
            i++;
        }

        return "추가 명령어를 입력하세요! : %s".formatted(String.join(", ", childNames));
    }

    private void checkCommandExecuted() {
        if (event == null) throw new IllegalStateException("Slash command event is null, is command executed?");
    }

    /**
     * Reply {@link #needSubCommand(Member)} message.
     */
    protected void callNeedSubCommand() {
        checkCommandExecuted();

        event.reply(needSubCommand(event.getMember())).queue();
    }

    @NotNull
    protected static String underConstruction() {
        return ":construction: 공사중...";
    }

    /**
     * Reply {@link #underConstruction()} message.
     */
    protected void callUnderConstruction() {
        checkCommandExecuted();

        event.reply(underConstruction()).queue();
    }

    /**
     * Get option and transform.
     *
     * <p>Example:
     * <pre>{@code
     *     Long number = getOptionLong("Number");
     *     if (number == null) return;
     * }</pre>
     *
     * @param transformer  transformer for option value
     * @param optionName   name of option
     * @param defaultValue default value if option value is null
     * @return string value of option
     */
    @Nullable(value = "if option is not required and defaultValue is null")
    protected <T> T getOption(@NotNull Function<OptionMapping, T> transformer,
                              @NotNull Class<T> targetClass,
                              @NotNull String optionName,
                              @Nullable T defaultValue) {
        checkCommandExecuted();

        OptionData optionData = getOptions()
                .stream()
                .filter(option -> option.getName().equals(optionName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("can't find option " + optionName));
        OptionMapping option = event.getOption(optionName);

        if (option == null) {
            if (optionData.isRequired() && defaultValue == null)
                throw new OptionNotFoundException(optionName);

            return defaultValue;
        }

        try {
            return transformer.apply(option);
        } catch (Exception e) {
            throw new OptionTypeMismatchException(optionName,
                    option.getAsString(),
                    targetClass.getSimpleName());
        }
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    @Contract("_, !null -> !null")
    protected String getOptionString(@NotNull String optionName,
                                     @Nullable String defaultValue) {
        return getOption(OptionMapping::getAsString, String.class, optionName, defaultValue);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    protected String getOptionString(@NotNull String optionName) {
        return getOptionString(optionName, null);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    @Contract("_, !null -> !null")
    protected Long getOptionLong(@NotNull String optionName,
                                 @Nullable Long defaultValue) {
        return getOption(OptionMapping::getAsLong, Long.class, optionName, defaultValue);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    protected Long getOptionLong(@NotNull String optionName) {
        return getOptionLong(optionName, null);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    @Contract("_, !null -> !null")
    protected Double getOptionDouble(@NotNull String optionName,
                                     @Nullable Double defaultValue) {
        return getOption(OptionMapping::getAsDouble, Double.class, optionName, defaultValue);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    protected Double getOptionDouble(@NotNull String optionName) {
        return getOptionDouble(optionName, null);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    @Contract("_, !null -> !null")
    protected Integer getOptionInt(@NotNull String optionName,
                                   @Nullable Integer defaultValue) {
        return getOption(OptionMapping::getAsInt, Integer.class, optionName, defaultValue);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    protected Integer getOptionInt(@NotNull String optionName) {
        return getOptionInt(optionName, null);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    @Contract("_, !null -> !null")
    protected User getOptionUser(@NotNull String optionName,
                                 @Nullable User defaultValue) {
        return getOption(OptionMapping::getAsUser, User.class, optionName, defaultValue);
    }

    /**
     * @see #getOption(Function, Class, String, Object)
     */
    protected User getOptionUser(@NotNull String optionName) {
        return getOptionUser(optionName, null);
    }

}
