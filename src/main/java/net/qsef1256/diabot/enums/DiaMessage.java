package net.qsef1256.diabot.enums;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.qsef1256.diabot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;

public class DiaMessage {

    /**
     * 추가 명령어 필요 안내 메시지를 얻습니다.
     *
     * @param children sub commands
     * @param member   executing member
     * @return 추가 명령어를 입력하세요! : 추가, 삭제
     */
    @NotNull
    public static String needSubCommand(SlashCommand @NotNull [] children, Member member) {
        String[] childNames = new String[children.length];

        int i = 0;
        for (SlashCommand child : children) {
            if (JDAUtil.canExecute(child, member)) childNames[i] = child.getName();
            i++;
        }
        return "추가 명령어를 입력하세요! : " + String.join(", ", childNames);
    }

    @NotNull
    public static String underConstruction() {
        return ":construction: 공사중...";
    }

}
