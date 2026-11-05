package sa.urtv.home;

import android.content.Context;

/** Whether RiyTv's main home screen is currently in front, so prayer-time banners can be skipped there. */
public final class HomePresence {
    private static final String PREFS = "urtv_home_presence";
    private static final String ACTIVE = "active";

    private HomePresence() { }

    public static void setActive(Context c, boolean active) {
        c.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putBoolean(ACTIVE, active).commit();
    }

    public static boolean isActive(Context c) {
        return c.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .getBoolean(ACTIVE, false);
    }
}
