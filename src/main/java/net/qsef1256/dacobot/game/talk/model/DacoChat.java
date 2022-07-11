package net.qsef1256.dacobot.game.talk.model;

import lombok.Getter;
import net.qsef1256.dacobot.setting.DiaSetting;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.configuration.BotConfiguration;
import org.alicebot.ab.configuration.Constants;
import org.alicebot.ab.configuration.LanguageConfiguration;
import org.alicebot.ab.model.Category;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.openmbean.KeyAlreadyExistsException;

// TODO: https://github.com/open-korean-text/open-korean-text ** CONSIDERING **
// TODO: https://howtodoinjava.com/java/library/java-aiml-chatbot-example/ ** CHOSEN THIS **
// TODO: https://www.tutorialspoint.com/aiml/aiml_quick_guide.htm
// TODO: http://www.aiml.foundation/doc.html
// TODO: https://code.google.com/archive/p/program-ab/
public class DacoChat {

    public static final Logger logger = LoggerFactory.getLogger("DacoChat");
    
    @Getter
    private Bot chatBot;
    private Chat chat;

    private DacoChat() {
        setUp();
    }

    private void setUp() {
        chatBot = new Bot(BotConfiguration.builder()
                .name("DacoChat")
                .programName("다코")
                .path(DiaSetting.getResourcesPath() + "/chatbot")
                .language(LanguageConfiguration.builder()
                        .errorResponse("에?러")
                        .defaultResponse("모?루")
                        .build())
                .build());

        chat = new Chat(chatBot);
        updateAIML();
    }

    public static DacoChat getInstance() {
        return LazyHolder.instance;
    }

    public String talk(@NotNull String message) {
        if (message.isBlank())
            message = Constants.null_input;

        return chat.multisentenceRespond(message);
    }

    // TODO: save the owner
    // TODO: <that>, <topic>, <srai>

    /**
     * 키워드를 추가합니다.
     * <p>
     * 키워드란 앞뒤로 어떤걸 포함하고 있던 똑같은 답변을 출력합니다. "사과" -> "맛있는 것." 같이 명사의 정보를 출력하는데 유용합니다.
     * </p>
     *
     * @param pattern  message
     * @param template response
     */
    public void addKeyword(String pattern, String template) {
        verifyInput(pattern, template);
        addCategory("# %s #".formatted(pattern), template);
        updateAIML();
    }

    /**
     * 카테고리를 하나 추가합니다. 카테고리에 대한 설명은 AIML 사양서를 참고하세요.
     *
     * @param pattern  AIML pattern (input)
     * @param template AIML template (output)
     * @see Category
     */
    public void addCategory(String pattern, String template) {
        verifyInput(pattern, template);
        Category category = new Category(chatBot, 0, pattern, "*", "*", template, "user.aiml");
        if (chatBot.getBrain().existsCategory(category))
            throw new KeyAlreadyExistsException("이미 그런 대화가 존재합니다!: 메시지, %s, 답변: %s".formatted(pattern, template));

        chatBot.getBrain().addCategory(category);
    }

    /**
     * 인스턴스에 입력된 카테고리들을 업데이트 합니다.
     * <p><b>주의!: </b>AIML 파일을 직접 수정한 경우 재시작 해야 합니다.
     *
     * @see DacoChat#restart()
     */
    public void updateAIML() {
        chatBot.writeAIMLFiles();
    }

    private void verifyInput(String message, String response) {
        logger.info("%s %s".formatted(message, response));

        if (message.isBlank() || response.isBlank())
            throw new IllegalArgumentException("message or response cannot be blank: message: %s, response: %s".formatted(message, response));
    }

    private static class LazyHolder {
        private static final DacoChat instance = new DacoChat();
    }

    /**
     * AIML 파일을 직접 수정한 경우 다코챗 인스턴스를 재시작 합니다.
     *
     * @see DacoChat#updateAIML()
     */
    public void restart() {
        logger.info("Restarting Daco Chat");

        chatBot = null;
        chat = null;
        setUp();
    }

}
