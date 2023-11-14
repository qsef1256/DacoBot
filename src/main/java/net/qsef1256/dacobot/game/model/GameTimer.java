package net.qsef1256.dacobot.game.model;

import net.qsef1256.dacobot.core.schedule.DiaScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class GameTimer { // TODO: use

    private final DiaScheduler scheduler;
    private int timeLimit;
    private Runnable stopTask;
    private ScheduledFuture<?> timer;

    public GameTimer(@Autowired DiaScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void refresh() {
        stopTimer();

        timer = scheduler.schedule(stopTask, timeLimit, TimeUnit.SECONDS);
    }

    public void stopTimer() {
        if (timer != null) timer.cancel(true);
    }

}
