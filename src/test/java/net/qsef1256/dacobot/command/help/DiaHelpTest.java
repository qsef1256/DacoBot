package net.qsef1256.dacobot.command.help;

import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class DiaHelpTest {

    @Mock
    private Member member;

    @Test
    void getMainMenu(@NotNull DiaHelp diaHelp) {
        diaHelp.getMainMenu(member);
    }

    @Test
    void getSearchResult(@NotNull DiaHelp diaHelp) {
        diaHelp.getSearchResult(member, "정보");
    }

}