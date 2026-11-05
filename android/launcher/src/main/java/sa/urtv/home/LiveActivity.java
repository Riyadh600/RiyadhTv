package sa.urtv.home;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/** Manage the user's YouTube live video links; the focused field accepts text from the paired phone remote. */
public final class LiveActivity extends Activity {
    private LinearLayout rows;
    private EditText name, url;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        root.setPadding(dp(28), dp(20), dp(28), dp(18));
        root.setBackgroundColor(0xFF101216);
        TextView title = label("البث المباشر", 30, 0xFFFFE9B8);
        root.addView(title);
        root.addView(label("أضف رابط بث يوتيوب. اكتب الاسم والرابط من لوحة المفاتيح أو من الريموت على الجوال.", 16, 0xFFD5D1C8), lp(-1, -2, 0, 0, 12));

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.HORIZONTAL);
        name = field("اسم القناة", 140);
        url = field("رابط يوتيوب مباشر", 360);
        url.setTextDirection(View.TEXT_DIRECTION_LTR);
        form.addView(name, new LinearLayout.LayoutParams(0, dp(52), 1f));
        form.addView(url, new LinearLayout.LayoutParams(0, dp(52), 2f));
        root.addView(form);
        TextView add = action("إضافة القناة");
        add.setOnClickListener(v -> addChannel());
        root.addView(add, lp(-1, dp(48), 0, 8, 14));

        ScrollView scroll = new ScrollView(this);
        rows = new LinearLayout(this);
        rows.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(rows);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1f));
        TextView back = action("رجوع");
        back.setOnClickListener(v -> finish());
        root.addView(back, lp(-1, dp(46), 0, 10, 0));
        setContentView(root);
        name.requestFocus();
        render();
    }

    private void addChannel() {
        if (!LiveChannels.add(this, name.getText().toString(), url.getText().toString())) {
            Toast.makeText(this, "تحقق من الاسم والرابط. اقبل روابط فيديو يوتيوب فقط، وبحد أقصى ٢٠ قناة.", Toast.LENGTH_LONG).show();
            return;
        }
        name.setText(""); url.setText(""); name.requestFocus(); render();
    }

    private void render() {
        rows.removeAllViews();
        List<LiveChannels.Channel> channels = LiveChannels.read(this);
        if (channels.isEmpty()) rows.addView(label("لا توجد قنوات بعد. أضف رابط فيديو يوتيوب يبدأ بـ https://", 17, 0xFFD5D1C8), lp(-1, dp(48), 0, 0, 0));
        for (LiveChannels.Channel c : channels) {
            LinearLayout line = new LinearLayout(this);
            line.setGravity(Gravity.CENTER_VERTICAL);
            TextView open = action(c.name);
            open.setContentDescription("شغّل " + c.name);
            open.setOnClickListener(v -> startActivity(LivePlayerActivity.intent(this, c.name, c.videoId)));
            line.addView(open, new LinearLayout.LayoutParams(0, dp(54), 1f));
            TextView remove = action("حذف");
            remove.setOnClickListener(v -> { LiveChannels.remove(this, c.videoId); render(); });
            line.addView(remove, new LinearLayout.LayoutParams(dp(112), dp(54)));
            rows.addView(line, lp(-1, -2, 0, 5, 5));
        }
    }

    private EditText field(String hint, int max) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setHint(hint);
        e.setTextColor(Color.WHITE);
        e.setHintTextColor(0xFF999999);
        e.setTextSize(17);
        e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        e.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(max)});
        e.setPadding(dp(12), 0, dp(12), 0);
        e.setBackgroundColor(0xFF252931);
        return e;
    }

    private TextView action(String text) {
        TextView t = label(text, 18, Color.WHITE);
        t.setFocusable(true); t.setClickable(true); t.setGravity(Gravity.CENTER);
        t.setBackground(GlassPaint.row(this, homeGlass(), 12));
        return t;
    }
    private GlassStyle.Look homeGlass() { return GlassSource.look(this); }
    private TextView label(String text, int size, int color) {
        TextView t = new TextView(this); t.setText(text); t.setTextColor(color); t.setTextSize(size);
        t.setGravity(Gravity.CENTER_VERTICAL); t.setPadding(dp(8), dp(2), dp(8), dp(2)); return t;
    }
    private LinearLayout.LayoutParams lp(int w, int h, int start, int top, int bottom) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w, h);
        p.setMarginStart(dp(start)); p.topMargin = dp(top); p.bottomMargin = dp(bottom); return p;
    }
    private int dp(float v) { return Math.round(v * getResources().getDisplayMetrics().density); }
}
