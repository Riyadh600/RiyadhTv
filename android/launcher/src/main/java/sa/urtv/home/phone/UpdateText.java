package sa.urtv.home.phone;

/** RiyTv's TV-side update messages. */
public final class UpdateText {
    public static final String TITLE = "تحديث تلفزيوني";
    public static final String CHECK = "تحقق من التحديث";
    public static final String OFFLINE = "ما فيه اتصال بالإنترنت";
    public static final String STARTING_INSTALL = "اكتمل التحقق. جارٍ فتح شاشة تثبيت التحديث…";
    public static final String INSTALL = "تثبيت التحديث";
    public static final String CANNOT_UPDATE = "تعذّر إكمال التحديث. حاول مرة أخرى.";
    public static final String NEED_PERMISSION = "لازم تسمح لتطبيق تلفزيون الرياض بتثبيت التحديثات.";
    public static final String OPEN_PERMISSION = "فتح شاشة السماح";
    public static final String WORKING = "جارٍ التحقق…";
    public static final String DOWNLOADING = "جارٍ تنزيل التحديث والتحقق منه…";
    public static final String CONFIRM = "أكّد التثبيت على شاشة أندرويد بالريموت.";
    public static final String SHOW_CONFIRM = "اعرض شاشة التثبيت";
    public static final String NOT_DONE = "ما اكتمل التحديث";
    public static final String DECLINED = "ألغيت التثبيت.";

    public static final String INSTALLED = "الإصدار المثبّت %s";
    public static final String UP_TO_DATE = "تلفزيونك محدّث: %s";
    public static final String AVAILABLE = "تحديث جديد %s · اضغط للتحديث";
    public static final String DONE = "تم التحديث ✓ %s";
    public static final String FAILED_STEP = "الخطوة: %s";

    /** A small seam for the selected UI language; no Android dependency. */
    public interface Say { String t(String template, Object... args); }
    private static final Say ARABIC = (template, args) -> String.format(template, args);

    private UpdateText() { }

    /** Keep the version together inside an Arabic line. */
    private static String isolate(String s) { return "⁧" + s + "⁩"; }

    public static String installed(String version) { return installed(version, ARABIC); }
    public static String installed(String version, Say say) { return say.t(INSTALLED, isolate(version)); }

    public static String upToDate(String version) { return "تلفزيون الرياض محدّث: " + isolate(version); }
    public static String upToDate(String version, Say say) { return say.t(UP_TO_DATE, isolate(version)); }

    public static String available(String version) { return available(version, ARABIC); }
    public static String available(String version, Say say) { return say.t(AVAILABLE, isolate(version)); }

    public static String done(String version) { return done(version, ARABIC); }
    public static String done(String version, Say say) { return say.t(DONE, isolate(version)); }

    public static String failedStep(String label) { return failedStep(label, ARABIC); }
    public static String failedStep(String label, Say say) { return say.t(FAILED_STEP, label); }

    public static String failure(String why) {
        if ("download".equals(why)) return "تعذّر تنزيل ملفات التحديث. تحقق من اتصال الإنترنت ثم أعد المحاولة.";
        if ("other_key".equals(why) || "unsigned".equals(why)) return "توقيع ملف التحديث لا يطابق هذا التطبيق؛ أوقفنا التثبيت لحمايته.";
        if ("other_app".equals(why) || "not_newer".equals(why)) return "ملف التحديث غير مناسب لهذه النسخة.";
        if ("space".equals(why) || "storage".equals(why)) return "مساحة التلفزيون غير كافية لإكمال التحديث.";
        if ("network".equals(why)) return "اتصل التلفزيون بالواي فاي أو الإيثرنت ثم أعد المحاولة.";
        if ("permission_screen".equals(why) || "need_permission".equals(why)) return "اسمح لتطبيق تلفزيون الرياض بتثبيت التحديثات من إعدادات أندرويد.";
        if ("installer".equals(why) || "blocked".equals(why) || "conflict".equals(why)) return "منع نظام التلفزيون تثبيت التحديث. أعد المحاولة أو راجع إعدادات التثبيت.";
        return CANNOT_UPDATE;
    }

    /** Keep the release name shown to users; build numbers remain in status and reports. */
    public static String versionOf(String marketing, int build) { return marketing; }
}
