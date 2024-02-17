package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.qsef1256.dacobot.core.command.CommandClientService;
import net.qsef1256.dacobot.module.cmdstat.CmdStatisticEntity;
import net.qsef1256.dacobot.module.cmdstat.CmdStatisticService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 뭐라 적지?
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

    protected String getUseInfo() {
        if (!statistic)
            throw new IllegalStateException("statistic is disabled, can't command use statistic info.");
        CmdStatisticEntity statistic = getCmdStatistic();

        return "금일: " + statistic.getTodayUsed() + " 총합: " + statistic.getUseCount();
    }

    /**
     * 추가 명령어 필요 안내 메시지를 얻습니다.
     *
     * @param member executing member
     * @return 추가 명령어를 입력하세요! : 추가, 삭제
     */
    @NotNull
    protected String needSubCommand(Member member) {
        String[] childNames = new String[children.length];

        int i = 0;
        for (Command child : children) {
            if (commandClientService.canExecute(child, member))
                childNames[i] = child.getName();
            i++;
        }

        return "추가 명령어를 입력하세요! : " + String.join(", ", childNames);
    }

    protected void callNeedSubCommand() {
        if (event == null)
            throw new IllegalStateException("Slash command event is null, is command executed?");

        event.reply(needSubCommand(event.getMember())).queue();
    }

    @NotNull
    protected static String underConstruction() {
        return ":construction: 공사중...";
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
     * @param transformer transformer for option value
     * @param optionName  name of option
     * @param required    if true and option are null, send notice and close interaction of event
     * @return string value of option
     */
    @Nullable
    protected <T> T getOption(@NotNull Function<OptionMapping, T> transformer,
                              @NotNull String optionName,
                              boolean required) {
        if (event == null)
            throw new IllegalStateException("Slash command event is null, is command executed?");

        OptionMapping option = event.getOption(optionName);
        if (option == null) {
            if (required)
                event.reply("%s를 입력해주세요.".formatted(optionName))
                        .setEphemeral(true)
                        .queue();
            return null;
        }

        return transformer.apply(option);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected String getOptionString(@NotNull String optionName, boolean required) {
        return getOption(OptionMapping::getAsString, optionName, required);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected String getOptionString(@NotNull String optionName) {
        return getOptionString(optionName, true);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected Long getOptionLong(@NotNull String optionName, boolean required) {
        return getOption(OptionMapping::getAsLong, optionName, required);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected Long getOptionLong(@NotNull String optionName) {
        return getOptionLong(optionName, true);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected Double getOptionDouble(@NotNull String optionName, boolean required) {
        return getOption(OptionMapping::getAsDouble, optionName, required);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected Double getOptionDouble(@NotNull String optionName) {
        return getOptionDouble(optionName, true);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected Integer getOptionInt(@NotNull String optionName, boolean required) {
        return getOption(OptionMapping::getAsInt, optionName, required);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected Integer getOptionInt(@NotNull String optionName) {
        return getOptionInt(optionName, true);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected User getOptionUser(@NotNull String optionName, boolean required) {
        return getOption(OptionMapping::getAsUser, optionName, required);
    }

    /**
     * @see #getOption(Function, String, boolean)
     */
    @Nullable
    protected User getOptionUser(@NotNull String optionName) {
        return getOptionUser(optionName, true);
    }

}
