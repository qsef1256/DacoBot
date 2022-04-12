package net.qsef1256.dacobot.game.talk.model;

import net.qsef1256.dacobot.setting.DiaSetting;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.configuration.Constants;
import org.alicebot.ab.utils.IOUtils;

class ChatConsole {

    private static final boolean TRACE_MODE = false;

    public static void main(String[] args) {
        try {
            String resourcesPath = DiaSetting.getResourcesPath();
            System.out.println(resourcesPath);

            Bot bot = DacoChat.getInstance().getChatBot();
            Chat chatSession = new Chat(bot);
            bot.getBrain().nodeStats();

            while (true) {
                System.out.print("Human : ");
                String textLine = IOUtils.readInputTextLine();
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
