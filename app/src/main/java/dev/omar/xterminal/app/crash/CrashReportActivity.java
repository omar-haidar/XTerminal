package dev.omar.xterminal.app.crash;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CrashReportActivity extends AppCompatActivity {
    private static final String EXTRA_CRASH_MESSAGE = "EXTRA_CRASH_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String message = "No error message!";

        if (getIntent().hasExtra(EXTRA_CRASH_MESSAGE)) {
            message = getIntent().getStringExtra(EXTRA_CRASH_MESSAGE);
        }
        final String finalMessage = message;
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle("An error occurred");
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Copy", (d, i) -> {
            ClipboardUtils.copyText("Error", finalMessage);
            ToastUtils.showShort("Copied!");
        });
        dialogBuilder.setNegativeButton("Exit", (d, i) -> {
            finish();
        });
        dialogBuilder.show();

    }

    public static void initCrashHandler(Application app) {
        if (app == null) return;
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            String message = Log.getStackTraceString(throwable);
            Intent intent = new Intent(app.getApplicationContext(), CrashReportActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(EXTRA_CRASH_MESSAGE, message);
            app.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });
    }
}