package net.qsef1256.dacobot.service.message.type;

public interface Timed {

    void refresh();

    void onTimeout();

}
