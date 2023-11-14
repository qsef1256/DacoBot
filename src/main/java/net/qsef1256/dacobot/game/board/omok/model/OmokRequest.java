package net.qsef1256.dacobot.game.board.omok.model;

import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.qsef1256.dacobot.module.request.model.Request;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class OmokRequest extends Request {

    @Setter(onMethod_ = {@Autowired})
    private OmokController omokController;

    protected OmokRequest(long requesterId,
                          long receiverId,
                          MessageChannel channel) {
        super(requesterId, receiverId, channel);
    }

    @Override
    public void accept() {
        omokController.createGame(this);
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
