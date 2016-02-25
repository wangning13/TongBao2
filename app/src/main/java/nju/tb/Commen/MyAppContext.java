package nju.tb.Commen;


import android.app.Application;

public class MyAppContext extends Application {
    private boolean isLogIn;

    public void setLogIn(boolean logState) {
        this.isLogIn = logState;
    }

    public boolean isLogIn() {
        return this.isLogIn;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLogIn = false;
    }
}
