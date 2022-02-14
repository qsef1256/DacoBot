package net.qsef1256.dacobot.command.fun.encrypt;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.enums.DiaColor;
import net.qsef1256.dacobot.enums.DiaEmbed;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static net.qsef1256.dacobot.DacoBot.logger;

public class MD5Command extends SlashCommand {

    public MD5Command() {
        name = "md5";
        help = "MD5는 암호화 알고리즘 중 하나로, 뜷렸습니다.";
        options = Collections.singletonList(new OptionData(OptionType.STRING, "메시지", "시킬 말").setRequired(true));
    }

    @NotNull
    public static String toMD5(@NotNull String content) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(content.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte byteDatum : byteData) {
            sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    @Override
    public void execute(final @NotNull SlashCommandEvent event) {
        if (event.getMember() == null) return;
        User user = event.getUser();

        final OptionMapping option = event.getOption("메시지");
        if (option == null) {
            event.reply("메시지를 입력해주세요.").setEphemeral(true).queue();
            return;
        }

        try {
            String MD5 = toMD5(option.getAsString());
            event.replyEmbeds(new EmbedBuilder()
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setColor(DiaColor.INFO)
                    .addField("MD5 변환기", "변환할 값: `" + option.getAsString() + "`\n변환된 값: `" + MD5 + "`", false)
                    .build()).queue();
        } catch (RuntimeException | NoSuchAlgorithmException e) {
            logger.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error(null, "주어진 문자열을 MD5로 변환하던 도중 문제가 생겼습니다.", null, null)
                    .setFooter("뭘 넣었길래...")
                    .build()).queue();
        }
    }
}