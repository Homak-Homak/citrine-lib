package org.carpentry.citrine.api.util.schedule;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.IntConsumer;

public class TickSchedulerServer {
    private static final List<ScheduledTask> tasks = new CopyOnWriteArrayList<>();
    private static final List<ScheduledTask> tasksToAdd = new ArrayList<>();
    private static final List<RepeatingTask> repeatingTasks = new CopyOnWriteArrayList<>();
    private static final List<RepeatingTask> repeatingTasksToAdd = new ArrayList<>();

    static {
        ServerTickEvents.END_SERVER_TICK.register(TickSchedulerServer::onTick);
    }

    /**
     * Schedules a task to execute after a specified delay in ticks
     * @param delayTicks Delay of how many ticks to wait before executing the action
     * @param action The action to execute each tick, receives current iteration count (0-indexed)
     */
    public static ScheduledTask schedule(int delayTicks, Runnable action) {
        ScheduledTask task = new ScheduledTask(delayTicks, action);
        tasksToAdd.add(task);
        return task;
    }

    /**
     * Schedules a task to execute repeatedly once per tick for the specified number of times
     * @param times Number of times to execute the action
     * @param action The action to execute each tick, receives current iteration count (0-indexed)
     */
    public static RepeatingTask scheduleRepeating(int times, IntConsumer action) {
        RepeatingTask task = new RepeatingTask(times, action);
        repeatingTasksToAdd.add(task);
        return task;
    }

    private static void onTick(MinecraftServer server) {
        if (!tasksToAdd.isEmpty()) {
            tasks.addAll(tasksToAdd);
            tasksToAdd.clear();
        }

        if (!repeatingTasksToAdd.isEmpty()) {
            repeatingTasks.addAll(repeatingTasksToAdd);
            repeatingTasksToAdd.clear();
        }

        for (ScheduledTask task : tasks) {
            task.ticksLeft--;
            if (task.ticksLeft <= 0) {
                try {
                    task.action.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tasks.remove(task);
            }
        }

        for (RepeatingTask task : repeatingTasks) {
            try {
                int currentIteration = task.totalExecutions - task.executionsLeft;
                task.action.accept(currentIteration);
            } catch (Exception e) {
                e.printStackTrace();
            }

            task.executionsLeft--;
            if (task.executionsLeft <= 0) {
                repeatingTasks.remove(task);
            }
        }
    }

    public static class ScheduledTask {
        int ticksLeft;
        Runnable action;

        ScheduledTask(int ticks, Runnable action) {
            this.ticksLeft = ticks;
            this.action = action;
        }

        public void cancel() {
            this.ticksLeft = -1;
        }
    }

    public static class RepeatingTask {
        int executionsLeft;
        int totalExecutions;
        IntConsumer action;

        RepeatingTask(int times, IntConsumer action) {
            this.executionsLeft = times;
            this.totalExecutions = times;
            this.action = action;
        }

        public void cancel() {
            this.executionsLeft = 0;
        }
    }
}