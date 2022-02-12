package net.qsef1256.dacobot.game.omok.model;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.qsef1256.dacobot.system.request.model.Request;
import org.jetbrains.annotations.NotNull;

public class OmokRequest extends Request {

    protected OmokRequest(long requesterId, long receiverId, MessageChannel channel) {
        super(requesterId, receiverId, channel);
    }

    @Override
    public void accept() {
        OmokManager.createGame(this);
    }

    @Override
    public void deny() {
    }

    @Override
    public @NotNull String getTitle() {
        return "오목 대전";
    }

    @Override
    public String getDesc() {
        return null;
    }

}
