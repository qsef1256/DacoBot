package net.qsef1256.dacobot.command.fun.encrypt;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.util.notification.DiaEmbed;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static net.qsef1256.dacobot.DacoBot.logger;

public class SHA256Command extends SlashCommand {

    public SHA256Command() {
        name = "sha";
        help = "SHA-256는 암호화 알고리즘 중 하나로, 인터넷 뱅킹에서 사용합니다.";
        options = Collections.singletonList(new OptionData(OptionType.STRING, "메시지", "시킬 말").setRequired(true));
    }

    @NotNull
    public static String toSHA256(@NotNull String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
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
            String sha256 = toSHA256(option.getAsString());
            event.replyEmbeds(DiaEmbed.info(null, null, user)
                    .addField("SHA-256 변환기", "변환할 값: `" + option.getAsString() + "`\n변환된 값: `" + sha256 + "`", false)
                    .build()).queue();
        } catch (RuntimeException | NoSuchAlgorithmException e) {
            logger.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error(null, "주어진 문자열을 SHA-256로 변환하던 도중 문제가 생겼습니다.", null, null)
                    .setFooter("뭘 넣었길래...")
                    .build()).queue();
        }
    }
}