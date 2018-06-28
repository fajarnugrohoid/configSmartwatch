package com.bms.user.bmssmartwatch;

import java.util.concurrent.ThreadFactory;

/**
 * Created by FAJAR-NB on 08/06/2018.
 */

public class SimpleThreadFactory implements ThreadFactory {
    private final String mThreadName;

    public SimpleThreadFactory(final String threadName) {
        super();
        mThreadName = threadName;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, mThreadName);
    }
}
