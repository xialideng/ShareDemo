package com.sfexpress.sharedemo;

public class HHT5UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultHandler;

    public void init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            ex.printStackTrace();
            handleException();
        } catch (Throwable fatality) {
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, ex);
            }
        }
    }

    private void handleException() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(-1);
    }
}
