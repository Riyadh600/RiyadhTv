package sa.urtv.home.tilawah;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import sa.urtv.home.HomePresence;
import sa.urtv.home.R;

/** A brief heads-up when prayer time begins in another foreground app; the RiyTv home keeps its existing adhan bar. */
final class PrayerTimeNotice {
    private static final String CHANNEL = "prayer_time_heads_up";
    private static final int NOTICE_ID = 7304;

    private PrayerTimeNotice() { }

    static void showIfAnotherAppIsInFront(Context c, int prayer) {
        if (HomePresence.isActive(c) || prayer < 0 || prayer >= PrayerTimes.NAMES.length) return;
        NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null || !manager.areNotificationsEnabled()) return;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANNEL, "تنبيهات أوقات الصلاة", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("تنبيه مختصر عند دخول وقت الصلاة أثناء استخدام تطبيق آخر");
            channel.setShowBadge(false);
            channel.setSound(null, null);
            channel.enableVibration(false);
            manager.createNotificationChannel(channel);
        }
        String prayerName = PrayerTimes.NAMES[prayer];
        if (prayer == PrayerTimes.DHUHR) {
            try { if (DayParts.now(c).friday) prayerName = "الجمعة"; }
            catch (RuntimeException ignored) { }
        }
        Notification.Builder b = Build.VERSION.SDK_INT >= 26
                ? new Notification.Builder(c, CHANNEL) : new Notification.Builder(c);
        Calendar now = Calendar.getInstance();
        String clock = new SimpleDateFormat("h:mm", new Locale("ar", "SA")).format(now.getTime());
        String period = now.get(Calendar.HOUR_OF_DAY) < 12 ? "صباحاً" : "مساءً";
        b.setSmallIcon(R.drawable.ic_note)
                .setContentTitle("دخل الآن وقت صلاة " + prayerName)
                .setContentText("الساعة " + clock + " " + period)
                .setCategory(Notification.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setTimeoutAfter(5_000L)
                .setDefaults(0);
        manager.notify(NOTICE_ID, b.build());
    }
}
