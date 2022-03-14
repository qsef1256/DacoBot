package net.qsef1256.dacobot.game.talk.model;

import lombok.Getter;
import net.qsef1256.dacobot.setting.DiaSetting;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.configuration.BotConfiguration;
import org.alicebot.ab.configuration.Constants;
import org.alicebot.ab.configuration.LanguageConfiguration;
import org.alicebot.ab.utils.IOUtils;
import org.jetbrains.annotations.NotNull;

// TODO: https://github.com/open-korean-text/open-korean-text ** CONSIDERING **
// TODO: https://howtodoinjava.com/java/library/java-aiml-chatbot-example/ ** CHOSEN THIS **
// TODO: https://www.popit.kr/how-to-make-korean-chatbot/
// TODO: https://www.tutorialspoint.com/aiml/aiml_quick_guide.htm
// TODO: http://www.aiml.foundation/doc.html
// TODO: https://code.google.com/archive/p/program-ab/
public class DacoChat {

    @Getter
    private final Bot chatBot = new Bot(BotConfiguration.builder()
            .name("DacoChat")
            .programName("다코")
            .path(DiaSetting.getResourcesPath() + "/chatbot")
            .language(LanguageConfiguration.builder()
                    .errorResponse("에?러")
                    .defaultResponse("모?루")
                    .build())
            .build());

    private final Chat chat = new Chat(chatBot);

    private DacoChat() {
        chatBot.getBrain().nodeStats();
        chatBot.writeAIMLFiles();
    }

    public static DacoChat getInstance() {
        return LazyHolder.instance;
    }

    public String talk(@NotNull String message) {
        if (message.isEmpty() | message.isBlank())
            message = Constants.null_input;

        return chat.multisentenceRespond(message);
    }

    private static class LazyHolder {
        private static final DacoChat instance = new DacoChat();
    }

}

class Chatbot {

    private static final boolean TRACE_MODE = false;

    public static void main(String[] args) {
        try {

            String resourcesPath = DiaSetting.getResourcesPath();
            System.out.println(resourcesPath);
            Bot bot = DacoChat.getInstance().getChatBot();
            Chat chatSession = new Chat(bot);
            bot.getBrain().nodeStats();
            String textLine;

            while (true) {
                System.out.print("Human : ");
                textLine = IOUtils.readInputTextLine();
                if ((textLine == null) || (textLine.length() < 1))
                    textLine = Constants.null_input;
                if (textLine.equals("q")) {
                    System.exit(0);
                } else if (textLine.equals("wq")) {
                    bot.writeQuit();
                    System.exit(0);
                } else {
                    if (TRACE_MODE)
                        System.out.println("STATE=" + textLine +
                                ":THAT=" + chatSession.getThatHistory().get(0).get(0) +
                                ":TOPIC=" + chatSession.getPredicates().get("topic"));
                    String response = chatSession.multisentenceRespond(textLine);
                    while (response.contains("&lt;"))
                        response = response.replace("&lt;", "<");
                    while (response.contains("&gt;"))
                        response = response.replace("&gt;", ">");
                    System.out.println("Robot : " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
