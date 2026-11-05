package sa.urtv.home;

/** Five coordinated accent themes for the photo based home screen. */
final class HomeTheme {
    private HomeTheme() { }

    static Look.Palette palette(String theme, boolean light) {
        Look.Palette base = Look.of(light);
        int accent;
        switch (theme == null ? "gold" : theme) {
            case "sea": accent = light ? 0xFF075B70 : 0xFF59D5E8; break;
            case "palm": accent = light ? 0xFF17613D : 0xFF75D69A; break;
            case "violet": accent = light ? 0xFF65448F : 0xFFD0B2FF; break;
            case "rose": accent = light ? 0xFF8B3551 : 0xFFFF9BB2; break;
            default: accent = light ? 0xFF5C3F0E : 0xFFD6AF6B; break;
        }
        int rgb = accent & 0xFFFFFF;
        int dark = 0x000000;
        int pale = 0xF4EEE2;
        switch (theme == null ? "gold" : theme) {
            case "sea": dark = 0x071820; pale = 0xE6F1F2; break;
            case "palm": dark = 0x07180F; pale = 0xEAF2E9; break;
            case "violet": dark = 0x160E20; pale = 0xF0EAF5; break;
            case "rose": dark = 0x200D14; pale = 0xF5E9EC; break;
            default: break;
        }
        int[] scrim = new int[base.scrim.length];
        int tint = light ? pale : dark;
        for (int i = 0; i < scrim.length; i++) scrim[i] = (base.scrim[i] & 0xFF000000) | tint;
        int shade = (base.shade & 0xFF000000) | tint;
        return new Look.Palette(base.text, base.ink, base.soft, accent, base.shadow, base.titleShadow, scrim,
                shade, (0x12 << 24) | rgb, (0x24 << 24) | rgb);
    }
}
