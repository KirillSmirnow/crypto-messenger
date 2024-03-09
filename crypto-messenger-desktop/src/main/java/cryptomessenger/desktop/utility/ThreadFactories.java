package cryptomessenger.desktop.utility;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadFactories {

    public static ThreadFactory daemon() {
        return runnable -> {
            var thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setDaemon(true);
            return thread;
        };
    }
}
