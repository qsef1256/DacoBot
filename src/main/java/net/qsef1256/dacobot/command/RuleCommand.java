package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class RuleCommand extends DacoCommand {

    public RuleCommand() {
        name = "규칙";
        help = "안 보고 사고쳐도 아마 상관 없을지도?";
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.replyEmbeds(new EmbedBuilder()
                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                .setColor(DiaColor.MAIN_COLOR)
                .setTitle("이용 규칙")
                .appendDescription(":one: 상식!\n")
                .appendDescription(":two: 버그 악용 금지, 발견하면 DM 으로 연락해주세요.\n")
                .appendDescription(":three: 언제든지 데이터는 손실/초기화/삭제 될 수 있어요.\n")
                .appendDescription(":four: 곡괭이 자꾸 들이대면 폭탄 터트릴꺼에요.\n")
                .appendDescription(":five: 본체는 아무 생각이 없어요.\n")
                .appendDescription(":six: 사용자 식별을 위해 디코 ID를 저장하고 있습니다.\n")
                .appendDescription(":seven: 디스코드 ToS 를 준수하세요.\n")
                .appendDescription(":eight: 재밌게 노세요!\n")
                .setFooter("사실 규칙은 반쯤 장식이에요.")
                .build()).queue();
    }

}
