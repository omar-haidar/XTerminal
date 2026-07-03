package dev.omar.xterminal;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import dev.omar.xterminal.app.crash.CrashReportActivity;
import dev.omar.xterminal.utils.Environment;

public class App extends Application {
    private static App sApp;

    @Override
    public void onCreate() {
        sApp = this;
        super.onCreate();
        CrashReportActivity.initCrashHandler(this);
        Utils.init(this);
        Environment.init(this);
    }

    public static App getApp() {
        return sApp;
    }
}
