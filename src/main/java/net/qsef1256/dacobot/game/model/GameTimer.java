package net.qsef1256.dacobot.game.model;

import net.qsef1256.dacobot.schedule.DiaScheduler;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameTimer {

    private final int timeLimit;
    private final Runnable stopTask;
    private ScheduledFuture<?> timer;

    public GameTimer(int timeLimit, Runnable stopTask) {
        this.timeLimit = timeLimit;
        this.stopTask = stopTask;
    }

    public void refresh() {
        stopTimer();

        timer = DiaScheduler.schedule(stopTask, timeLimit, TimeUnit.SECONDS);
    }

    public void stopTimer() {
        if (timer != null) timer.cancel(true);
    }

}
