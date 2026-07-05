package dev.omar.xterminal.app.crash;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import dev.omar.xterminal.databinding.ActivityCrashReportBinding;

public class CrashReportActivity extends AppCompatActivity {
    private static final String EXTRA_CRASH_MESSAGE = "EXTRA_CRASH_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCrashReportBinding binding = ActivityCrashReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String message = "No error message!";

        if (getIntent().hasExtra(EXTRA_CRASH_MESSAGE)) {
            message = getIntent().getStringExtra(EXTRA_CRASH_MESSAGE);
        }
        binding.logText.setText(message);
        binding.closeButton.setOnClickListener(v -> {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        });

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