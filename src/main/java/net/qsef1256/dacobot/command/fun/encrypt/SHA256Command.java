package net.qsef1256.dacobot.command.fun.encrypt;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@Slf4j
@Component
public class SHA256Command extends DacoCommand {

    public SHA256Command() {
        name = "sha";
        help = "SHA-256는 암호화 알고리즘 중 하나로, 인터넷 뱅킹 등에서 사용합니다.";
        options = Collections.singletonList(
                new OptionData(OptionType.STRING, "메시지", "시킬 말").setRequired(true)
        );
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
    public void runCommand(@NotNull SlashCommandEvent event) {
        User user = event.getUser();
        String message = getOptionString("메시지");
        if (message == null) return;

        try {
            String sha256 = toSHA256(message);
            event.replyEmbeds(DiaEmbed.info(null, null, user)
                    .addField("SHA-256 변환기", "변환할 값: `" + message + "`\n변환된 값: `" + sha256 + "`", false)
                    .build()).queue();
        } catch (RuntimeException | NoSuchAlgorithmException e) {
            log.warn(e.getMessage());
            event.replyEmbeds(DiaEmbed.error(null, "주어진 문자열을 SHA-256로 변환하던 도중 문제가 생겼습니다.", null, null)
                    .setFooter("뭘 넣었길래...")
                    .build()).queue();
        }
    }

}
