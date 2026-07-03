package dev.omar.xterminal.app.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.termux.terminal.TerminalSession;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

import dev.omar.xterminal.MainActivity;
import dev.omar.xterminal.R;

public class SessionService extends Service {


    private static final String CHANNEL_ID = "XTerminal:SESSION_CHANNEL";
    private final Map<String, TerminalSession> sessions = new HashMap<>();
    private final Map<String, Integer> sessionList = new HashMap<>();
    private NotificationManager notificationManager;
    private final SessionBinder binder = new SessionBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class SessionBinder extends Binder {
        public SessionService getService() {
            return SessionService.this;
        }

        public void terminateAllSessions() {
            sessions.values().forEach(TerminalSession::finishIfRunning);
            sessions.clear();
            sessionList.clear();

        }

        public TerminalSession createSession() {

            return null;
        }

        public TerminalSession getSession(String id) {
            return sessions.get(id);
        }

        public void terminateSession(String id) {
            try {
                if (sessions.get(id).getEmulator() != null) {
                    sessions.get(id).finishIfRunning();
                }
                sessions.remove(id);
                sessionList.remove(id);
                if(sessions.isEmpty()){
                    stopSelf();
                }else {
                    updateNotification();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        sessions.values().forEach(TerminalSession::finishIfRunning);
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        Notification notification = createNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(2000, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(2000, notification);
        }
    }

    @NonNull
    private Notification createNotification() {
        var intent = new Intent(this, MainActivity.class);
        var perndingIntent = PendingIntent.getActivity(
                this,
                2000,
                intent,
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        var exitIntent = new Intent(this, SessionService.class);
        exitIntent.setAction("ACTION_EXIT");
        var exitPerndingIntent = PendingIntent.getService(
                this,
                2000,
                exitIntent,
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);


        return new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("XTerminal")
                .setContentText(getNotificationContent())
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentIntent(perndingIntent)
                .addAction(new NotificationCompat.Action(null,"Exit",exitPerndingIntent))
                .setOngoing(true)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Session Service", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void updateNotification() {
        var notification = createNotification();
        notificationManager.notify(2000, notification);

    }

    @NonNull
    @Contract(pure = true)
    private String getNotificationContent() {
        int count = sessions.size();
        if (count == 1) {
            return "1 session running";
        }
        return count + "sessions running";
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals("ACTION_EXIT")) {
                sessions.values().forEach(TerminalSession::finishIfRunning);
                stopSelf();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


}
