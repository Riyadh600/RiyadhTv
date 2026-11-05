package sa.urtv.home.phone;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Which release «تحديث تلفزيوني» reads: "latest" (always, unless an agent changed it) or "next", the test build. The screensaver keeps the
 * word (`optimize/saver.sh updatechannel`, no screen shows it) and tells it through its ConfigProvider (same key); an older screensaver, or
 * any answer but "next", is the latest.
 */
final class UpdateChannel {
    private UpdateChannel() { }

    static String read(Context c) {
        try (Cursor cur = c.getContentResolver().query(Uri.parse("content://sa.urtv.screensaver.config"), null, null, null, null)) {
            if (cur != null && cur.moveToFirst()) {
                int i = cur.getColumnIndex("update_channel");
                if (i >= 0 && "next".equals(cur.getString(i))) return "next";
            }
        } catch (RuntimeException ignored) {
            // the screensaver is missing or signed with another key: the latest
        }
        // A fresh test install has no saved channel yet; don't send it to the stable release it was not built from.
        return "next".equals(BuildConfig.BRANCH) ? "next" : "latest";
    }
}
