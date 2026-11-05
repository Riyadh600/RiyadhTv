package sa.urtv.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Owner-managed YouTube live video links, stored on this TV and accepted from the phone remote's text field. */
final class LiveChannels {
    static final int MAX = 20;
    static final class Channel {
        final String name, videoId;
        Channel(String name, String videoId) { this.name = name; this.videoId = videoId; }
    }
    private LiveChannels() { }

    static List<Channel> read(Context c) {
        List<Channel> out = new ArrayList<>();
        try {
            JSONArray a = new JSONArray(prefs(c).getString("liveChannels", "[]"));
            for (int i = 0; i < a.length() && out.size() < MAX; i++) {
                JSONObject o = a.optJSONObject(i);
                if (o == null) continue;
                String name = o.optString("name", ""), id = o.optString("id", "");
                if (validName(name) && validId(id)) out.add(new Channel(name, id));
            }
        } catch (Exception ignored) { }
        return out;
    }

    static boolean add(Context c, String name, String url) {
        String clean = name == null ? "" : name.trim();
        String id = videoId(url);
        if (!validName(clean) || id == null) return false;
        List<Channel> list = read(c);
        for (Channel ch : list) if (ch.videoId.equals(id)) return false;
        if (list.size() >= MAX) return false;
        list.add(new Channel(clean, id));
        return write(c, list);
    }

    static void remove(Context c, String id) {
        List<Channel> list = read(c);
        for (int i = list.size() - 1; i >= 0; i--) if (list.get(i).videoId.equals(id)) list.remove(i);
        write(c, list);
    }

    static String videoId(String raw) {
        if (raw == null || raw.trim().length() > 512) return null;
        try {
            Uri u = Uri.parse(raw.trim());
            if (!"https".equalsIgnoreCase(u.getScheme())) return null;
            String host = u.getHost();
            if (host == null) return null;
            host = host.toLowerCase(java.util.Locale.ROOT);
            if (!(host.equals("youtube.com") || host.endsWith(".youtube.com") || host.equals("youtu.be"))) return null;
            String id = u.getQueryParameter("v");
            if (id == null && host.equals("youtu.be")) id = u.getLastPathSegment();
            if (id == null) {
                String[] parts = u.getPathSegments().toArray(new String[0]);
                if (parts.length == 2 && ("live".equals(parts[0]) || "embed".equals(parts[0]) || "shorts".equals(parts[0]))) id = parts[1];
            }
            return validId(id) ? id : null;
        } catch (RuntimeException e) { return null; }
    }

    private static boolean validName(String s) {
        if (s == null || s.trim().isEmpty() || s.length() > 40) return false;
        for (int i = 0; i < s.length(); i++) if (Character.isISOControl(s.charAt(i))) return false;
        return true;
    }
    private static boolean validId(String s) { return s != null && s.matches("[A-Za-z0-9_-]{11}"); }

    private static SharedPreferences prefs(Context c) { return Dock.prefs(c); }
    private static boolean write(Context c, List<Channel> list) {
        JSONArray a = new JSONArray();
        try {
            for (Channel ch : list) a.put(new JSONObject().put("name", ch.name).put("id", ch.videoId));
        } catch (Exception e) { return false; }
        return prefs(c).edit().putString("liveChannels", a.toString()).commit();
    }
}
