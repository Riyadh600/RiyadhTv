package sa.urtv.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/** Full-screen YouTube embed for a user-saved video/live URL; channel data is reduced to a validated 11-character video id. */
public final class LivePlayerActivity extends Activity {
    static Intent intent(Context c, String name, String id) {
        return new Intent(c, LivePlayerActivity.class).putExtra("name", name).putExtra("id", id);
    }
    private WebView web;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        String id = getIntent().getStringExtra("id");
        if (id == null || !id.matches("[A-Za-z0-9_-]{11}")) { finish(); return; }
        web = new WebView(this);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());
        web.setBackgroundColor(0xFF000000);
        setContentView(web);
        String html = "<!doctype html><html><head><meta name='viewport' content='width=device-width,initial-scale=1,maximum-scale=1'>"
                + "<style>html,body{margin:0;width:100%;height:100%;background:#000;overflow:hidden}iframe{border:0;width:100%;height:100%}</style></head><body>"
                + "<iframe src='https://www.youtube-nocookie.com/embed/" + id + "?autoplay=1&controls=1&playsinline=0&rel=0' "
                + "allow='autoplay; encrypted-media; picture-in-picture' allowfullscreen></iframe></body></html>";
        web.loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "UTF-8", null);
    }

    @Override public void onBackPressed() { finish(); }
    @Override protected void onDestroy() { if (web != null) { web.stopLoading(); web.destroy(); web = null; } super.onDestroy(); }
}
