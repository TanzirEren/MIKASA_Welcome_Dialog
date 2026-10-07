package com.modmase.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;

public final class MIKASA {

    private static final String PREFS = "modmase_dialog";
    private static final String JOINED_KEY = "telegram_joined";

    private static final String TELEGRAM_URL = "https://t.me/MODMASE";
    private static final String VIDEO_NAME = "wlc_video.mp4";
    private static final String IMAGE_NAME = "mikasa.png";

    private static final int CREAM = Color.rgb(245, 240, 229);
    private static final int BLACK = Color.rgb(23, 21, 21);
    private static final int CHARCOAL = Color.rgb(47, 42, 40);
    private static final int RED = Color.rgb(173, 46, 68);
    private static final int MUTED = Color.rgb(117, 109, 101);
    private static final int FOOTER = Color.rgb(151, 143, 134);
    private static final int BORDER = Color.argb(42, 23, 21, 21);

    private MIKASA() {
    }

    public static void show(final Activity activity) {

        if (activity == null || activity.isFinishing()) {
            return;
        }

        SharedPreferences prefs =
                activity.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        if (prefs.getBoolean(JOINED_KEY, false)) {
            return;
        }

        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // TextureView (video) needs hardware acceleration. Some modded host
        // apps disable it, so force it on for the dialog window.
        if (dialog.getWindow() != null) {
            dialog.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
            );
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // =========================
        // ROOT
        // =========================

        FrameLayout root = new FrameLayout(activity);
        root.setPadding(
                dp(activity, 12),
                0,
                dp(activity, 12),
                0
        );

        // =========================
        // CARD
        // =========================

        final LinearLayout card = new LinearLayout(activity);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setElevation(dp(activity, 10));
        card.setClipToOutline(true);

        card.setBackground(
                rounded(
                        activity,
                        CREAM,
                        25,
                        1,
                        Color.argb(55, 255, 255, 255)
                )
        );

        FrameLayout.LayoutParams cardParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        cardParams.gravity = Gravity.CENTER;

        root.addView(card, cardParams);

        // =========================
        // MEDIA FRAME
        // =========================

        final FrameLayout mediaFrame = new FrameLayout(activity);

        mediaFrame.setBackgroundColor(
                Color.rgb(37, 33, 31)
        );

        // =========================
        // VIDEO (TextureView, sits under the cover image)
        // =========================

        final TextureView textureView = new TextureView(activity);

        textureView.setAlpha(0f);

        mediaFrame.addView(
                textureView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        // =========================
        // IMAGE (cover + fallback, fades out when video renders)
        // =========================

        final ImageView imageView = new ImageView(activity);

        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP
        );

        try {
            InputStream input =
                    activity.getAssets().open(IMAGE_NAME);

            imageView.setImageBitmap(
                    BitmapFactory.decodeStream(input)
            );

            input.close();

        } catch (Exception ignored) {
        }

        mediaFrame.addView(
                imageView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );

        // =========================
        // ACCENT
        // =========================

        View accent = new View(activity);

        accent.setBackground(
                rounded(
                        activity,
                        RED,
                        4,
                        0,
                        Color.TRANSPARENT
                )
        );

        FrameLayout.LayoutParams accentParams =
                new FrameLayout.LayoutParams(
                        dp(activity, 65),
                        dp(activity, 4)
                );

        accentParams.gravity =
                Gravity.BOTTOM | Gravity.START;

        accentParams.leftMargin =
                dp(activity, 42);

        mediaFrame.addView(accent, accentParams);

        LinearLayout.LayoutParams mediaParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(activity, 220)
                );

        card.addView(mediaFrame, mediaParams);

        // =========================
        // CONTENT
        // =========================

        LinearLayout content = new LinearLayout(activity);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);

        content.setPadding(
                dp(activity, 24),
                dp(activity, 22),
                dp(activity, 24),
                dp(activity, 20)
        );

        card.addView(
                content,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        // =========================
        // FONTS
        // =========================

        Typeface titleFont =
                loadFont(
                        activity,
                        "fonts/MikasaTitle.otf",
                        Typeface.SERIF
                );

        Typeface bodyFont =
                loadFont(
                        activity,
                        "fonts/MikasaBody.ttf",
                        Typeface.DEFAULT
                );

        Typeface bodyBoldFont =
                loadFont(
                        activity,
                        "fonts/MikasaBodyBold.ttf",
                        Typeface.DEFAULT_BOLD
                );

        // =========================
        // WELCOME
        // =========================

        TextView welcome =
                text(
                        activity,
                        "WELCOME",
                        RED,
                        10,
                        bodyBoldFont
                );

        welcome.setGravity(Gravity.CENTER);
        welcome.setLetterSpacing(0.26f);

        content.addView(welcome);

        // =========================
        // TITLE
        // =========================

        TextView title =
                text(
                        activity,
                        "MODMASE",
                        BLACK,
                        42,
                        titleFont
                );

        title.setGravity(Gravity.CENTER);
        title.setLetterSpacing(0.07f);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        titleParams.topMargin = dp(activity, 2);

        content.addView(title, titleParams);

        // =========================
        // DESCRIPTION
        // =========================

        TextView description =
                text(
                        activity,
                        "Discover updates, resources and creative content from MODMASE. "
                                + "Join our Telegram community and stay connected with the latest releases.",
                        MUTED,
                        13.5f,
                        bodyFont
                );

        description.setGravity(Gravity.CENTER);
        description.setLineSpacing(0, 1.38f);

        LinearLayout.LayoutParams descriptionParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        descriptionParams.topMargin = dp(activity, 10);
        descriptionParams.bottomMargin = dp(activity, 20);

        content.addView(
                description,
                descriptionParams
        );

        // =========================
        // BUTTONS
        // =========================

        LinearLayout buttons = new LinearLayout(activity);

        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setGravity(Gravity.CENTER);
        buttons.setWeightSum(2f);

        content.addView(
                buttons,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(activity, 50)
                )
        );

        Button exitButton =
                button(
                        activity,
                        "EXIT",
                        CHARCOAL,
                        bodyBoldFont
                );

        exitButton.setBackground(
                rounded(
                        activity,
                        Color.TRANSPARENT,
                        13,
                        1,
                        BORDER
                )
        );

        exitButton.setAllCaps(false);

        LinearLayout.LayoutParams exitParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(activity, 50),
                        0.8f
                );

        exitParams.rightMargin = dp(activity, 5);

        buttons.addView(exitButton, exitParams);

        Button telegramButton =
                button(
                        activity,
                        "JOIN TELEGRAM",
                        Color.WHITE,
                        bodyBoldFont
                );

        telegramButton.setBackground(
                rounded(
                        activity,
                        RED,
                        13,
                        0,
                        Color.TRANSPARENT
                )
        );

        telegramButton.setAllCaps(false);

        LinearLayout.LayoutParams telegramParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(activity, 50),
                        1.2f
                );

        telegramParams.leftMargin = dp(activity, 5);

        buttons.addView(
                telegramButton,
                telegramParams
        );

        // =========================
        // FOOTER
        // =========================

        TextView footer =
                text(
                        activity,
                        "MODMASE COMMUNITY",
                        FOOTER,
                        8.5f,
                        bodyBoldFont
                );

        footer.setGravity(Gravity.CENTER);
        footer.setLetterSpacing(0.20f);

        LinearLayout.LayoutParams footerParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        footerParams.topMargin = dp(activity, 17);

        content.addView(footer, footerParams);

        // =========================================================
        // VIDEO LOADER
        // =========================================================

        final VideoController video =
                new VideoController(
                        activity,
                        textureView,
                        imageView
                );

        video.start();

        // =========================
        // EXIT
        // =========================

        exitButton.setOnClickListener(v -> {

            press(v);

            video.release();

            v.postDelayed(() -> {

                if (!activity.isFinishing()) {

                    activity.finishAffinity();

                    if (Build.VERSION.SDK_INT >= 21) {
                        activity.finishAndRemoveTask();
                    }
                }

            }, 120);
        });

        // =========================
        // TELEGRAM
        // =========================

        telegramButton.setOnClickListener(v -> {

            press(v);

            prefs.edit()
                    .putBoolean(JOINED_KEY, true)
                    .apply();

            v.postDelayed(() -> {

                openTelegram(activity);

                dialog.dismiss();

            }, 120);
        });

        // =========================
        // DIALOG
        // =========================

        dialog.setOnDismissListener(d -> video.release());

        dialog.setContentView(root);

        dialog.setOnShowListener(d -> {

            Window window = dialog.getWindow();

            if (window == null) {
                return;
            }

            window.setBackgroundDrawableResource(
                    android.R.color.transparent
            );

            window.addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND
            );

            WindowManager.LayoutParams lp =
                    window.getAttributes();

            lp.dimAmount = 0.56f;

            window.setAttributes(lp);

            int screenWidth =
                    activity.getResources()
                            .getDisplayMetrics()
                            .widthPixels;

            int maxWidth =
                    dp(activity, 470);

            int width =
                    Math.min(
                            maxWidth,
                            (int) (screenWidth * 0.88f)
                    );

            window.setLayout(
                    width,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );

            // 16:9 media area
            mediaFrame.post(() -> {

                int w = mediaFrame.getWidth();

                if (w > 0) {

                    int h =
                            Math.round(
                                    w * 675f / 1200f
                            );

                    ViewGroup.LayoutParams p =
                            mediaFrame.getLayoutParams();

                    p.height = h;

                    mediaFrame.setLayoutParams(p);
                }
            });

            // =========================
            // CARD ANIMATION
            // =========================

            card.setAlpha(0f);
            card.setScaleX(0.94f);
            card.setScaleY(0.94f);
            card.setTranslationY(
                    dp(activity, 30)
            );

            card.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .translationY(0f)
                    .setDuration(620)
                    .setInterpolator(
                            new DecelerateInterpolator(1.8f)
                    )
                    .start();

            animateIn(welcome, 180, 8);
            animateIn(title, 240, 12);
            animateIn(description, 320, 10);
            animateIn(buttons, 390, 10);
            animateIn(footer, 480, 8);
        });

        dialog.show();
    }

    // =========================================================
    // VIDEO
    // =========================================================

    private static final String[] VIDEO_EXTENSIONS = {
            ".mp4", ".m4v", ".mov", ".3gp", ".webm", ".mkv"
    };

    private static boolean isVideoName(String name) {

        String n = name.toLowerCase(Locale.ROOT);

        for (String ext : VIDEO_EXTENSIONS) {
            if (n.endsWith(ext)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the video inside assets. The exact name wlc_video.mp4 is tried
     * first; if it is not there (for example the file was added with another
     * name or inside a sub-folder using MT Manager) the first video file
     * found in assets is used instead.
     */
    private static String findVideoAsset(
            AssetManager assets,
            String dir,
            int depth
    ) {

        if (dir.isEmpty()) {

            try {
                InputStream test = assets.open(VIDEO_NAME);
                test.close();
                return VIDEO_NAME;
            } catch (Exception ignored) {
            }
        }

        String[] items;

        try {
            items = assets.list(dir);
        } catch (Exception e) {
            return null;
        }

        if (items == null) {
            return null;
        }

        for (String item : items) {

            String path = dir.isEmpty() ? item : dir + "/" + item;

            if (isVideoName(item)) {
                return path;
            }
        }

        if (depth > 0) {

            for (String item : items) {

                String path = dir.isEmpty() ? item : dir + "/" + item;

                String found = findVideoAsset(assets, path, depth - 1);

                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    /**
     * Plays the looping, muted welcome video on a TextureView.
     *
     * Why TextureView and not VideoView:
     *  - VideoView is a SurfaceView. A SurfaceView that starts as GONE never
     *    creates its surface, so the video never prepares and never shows.
     *  - A SurfaceView also renders black / invisible inside animated,
     *    scaled or clipped parents (this dialog card animates in).
     *  TextureView works correctly with all of that.
     */
    private static final class VideoController
            implements TextureView.SurfaceTextureListener {

        private final Activity activity;
        private final TextureView textureView;
        private final ImageView cover;

        private MediaPlayer player;
        private Surface surface;

        private volatile File videoFile;
        private volatile boolean released;

        private boolean shown;
        private int videoWidth;
        private int videoHeight;

        VideoController(
                Activity activity,
                TextureView textureView,
                ImageView cover
        ) {
            this.activity = activity;
            this.textureView = textureView;
            this.cover = cover;
        }

        void start() {

            textureView.setSurfaceTextureListener(this);

            textureView.addOnLayoutChangeListener(
                    (v, l, t, r, b, ol, ot, or, ob) -> applyCenterCrop()
            );

            // Copy the video out of assets on a background thread
            // so the dialog opens instantly.
            new Thread(() -> {

                File file = null;

                try {
                    file = copyVideoToCache();
                } catch (Throwable ignored) {
                }

                final File result = file;

                activity.runOnUiThread(() -> {

                    if (released) {
                        return;
                    }

                    if (result == null) {
                        // No video in assets -> keep the image.
                        return;
                    }

                    videoFile = result;
                    tryPlay();
                });

            }, "mikasa-video-copy").start();
        }

        private File copyVideoToCache() throws Exception {

            AssetManager assets = activity.getAssets();

            String assetPath = findVideoAsset(assets, "", 2);

            if (assetPath == null) {
                return null;
            }

            File out = new File(activity.getCacheDir(), VIDEO_NAME);
            File tmp = new File(activity.getCacheDir(), VIDEO_NAME + ".tmp");

            InputStream input = assets.open(assetPath);
            FileOutputStream output = new FileOutputStream(tmp);

            try {
                byte[] buffer = new byte[16384];
                int length;

                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }

                output.flush();

            } finally {
                try { output.close(); } catch (Exception ignored) { }
                try { input.close(); } catch (Exception ignored) { }
            }

            if (tmp.length() == 0) {
                tmp.delete();
                return null;
            }

            if (out.exists()) {
                out.delete();
            }

            if (!tmp.renameTo(out)) {
                return null;
            }

            return out;
        }

        private void tryPlay() {

            if (released || player != null || videoFile == null) {
                return;
            }

            if (!textureView.isAvailable()) {
                return; // onSurfaceTextureAvailable will call us again
            }

            SurfaceTexture texture = textureView.getSurfaceTexture();

            if (texture == null) {
                return;
            }

            try {

                surface = new Surface(texture);

                MediaPlayer mp = new MediaPlayer();

                player = mp;

                mp.setSurface(surface);
                mp.setDataSource(videoFile.getAbsolutePath());
                mp.setLooping(true);
                mp.setVolume(0f, 0f);

                mp.setOnVideoSizeChangedListener((m, w, h) -> {
                    videoWidth = w;
                    videoHeight = h;
                    applyCenterCrop();
                });

                mp.setOnInfoListener((m, what, extra) -> {

                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        showVideo();
                    }

                    return false;
                });

                mp.setOnPreparedListener(m -> {

                    if (released || player != m) {
                        return;
                    }

                    try {
                        m.start();
                    } catch (Exception e) {
                        fallbackToImage();
                    }
                });

                mp.setOnErrorListener((m, what, extra) -> {
                    fallbackToImage();
                    return true;
                });

                mp.prepareAsync();

            } catch (Exception e) {
                fallbackToImage();
            }
        }

        private void showVideo() {

            if (shown || released) {
                return;
            }

            shown = true;

            applyCenterCrop();

            textureView.setScaleX(1.05f);
            textureView.setScaleY(1.05f);

            textureView.animate()
                    .alpha(1f)
                    .scaleX(1.02f)
                    .scaleY(1.02f)
                    .setDuration(700)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();

            cover.animate()
                    .alpha(0f)
                    .setDuration(500)
                    .withEndAction(() -> cover.setVisibility(View.GONE))
                    .start();
        }

        private void fallbackToImage() {

            stopPlayer();

            textureView.setAlpha(0f);

            cover.animate().cancel();
            cover.setAlpha(1f);
            cover.setVisibility(View.VISIBLE);

            shown = false;
        }

        /** Center-crop the video inside the TextureView (no stretching). */
        private void applyCenterCrop() {

            int viewW = textureView.getWidth();
            int viewH = textureView.getHeight();

            if (viewW == 0 || viewH == 0
                    || videoWidth == 0 || videoHeight == 0) {
                return;
            }

            float viewAspect = (float) viewW / viewH;
            float videoAspect = (float) videoWidth / videoHeight;

            float scaleX = 1f;
            float scaleY = 1f;

            if (videoAspect > viewAspect) {
                scaleX = videoAspect / viewAspect;
            } else {
                scaleY = viewAspect / videoAspect;
            }

            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY, viewW / 2f, viewH / 2f);

            textureView.setTransform(matrix);
        }

        private void stopPlayer() {

            MediaPlayer mp = player;

            player = null;

            if (mp != null) {
                try { mp.setOnErrorListener(null); } catch (Exception ignored) { }
                try { mp.setOnInfoListener(null); } catch (Exception ignored) { }
                try { mp.stop(); } catch (Exception ignored) { }
                try { mp.release(); } catch (Exception ignored) { }
            }

            if (surface != null) {
                try { surface.release(); } catch (Exception ignored) { }
                surface = null;
            }
        }

        void release() {

            released = true;

            stopPlayer();
        }

        // ---- TextureView.SurfaceTextureListener ----

        @Override
        public void onSurfaceTextureAvailable(
                SurfaceTexture st, int width, int height
        ) {
            tryPlay();
        }

        @Override
        public void onSurfaceTextureSizeChanged(
                SurfaceTexture st, int width, int height
        ) {
            applyCenterCrop();
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture st) {
            // App went to background / dialog closing. Free the player;
            // it restarts automatically if the surface comes back.
            stopPlayer();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture st) {

            if (!shown && player != null) {
                showVideo();
            }
        }
    }

    // =========================================================
    // TELEGRAM
    // =========================================================

    private static void openTelegram(
            Activity activity
    ) {

        try {

            Intent telegram =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                    "tg://resolve?domain=MODMASE"
                            )
                    );

            activity.startActivity(telegram);

            return;

        } catch (Exception ignored) {
        }

        try {

            Intent browser =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(TELEGRAM_URL)
                    );

            activity.startActivity(browser);

        } catch (Exception ignored) {
        }
    }

    // =========================================================
    // UI HELPERS
    // =========================================================

    private static TextView text(
            Context context,
            String value,
            int color,
            float size,
            Typeface typeface
    ) {

        TextView t =
                new TextView(context);

        t.setText(value);
        t.setTextColor(color);
        t.setTextSize(size);
        t.setTypeface(typeface);

        return t;
    }

    private static Button button(
            Context context,
            String value,
            int color,
            Typeface typeface
    ) {

        Button b =
                new Button(context);

        b.setText(value);
        b.setTextColor(color);
        b.setTextSize(11);
        b.setTypeface(typeface);
        b.setGravity(Gravity.CENTER);

        b.setPadding(
                0,
                0,
                0,
                0
        );

        b.setMinHeight(0);
        b.setMinWidth(0);

        return b;
    }

    private static Typeface loadFont(
            Context context,
            String path,
            Typeface fallback
    ) {

        try {

            return Typeface.createFromAsset(
                    context.getAssets(),
                    path
            );

        } catch (Exception e) {

            return fallback;
        }
    }

    private static GradientDrawable rounded(
            Context context,
            int color,
            float radiusDp,
            int strokeWidth,
            int strokeColor
    ) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);

        drawable.setCornerRadius(
                dp(context, radiusDp)
        );

        if (strokeWidth > 0) {

            drawable.setStroke(
                    dp(context, strokeWidth),
                    strokeColor
            );
        }

        return drawable;
    }

    private static void press(View view) {

        view.animate()
                .scaleX(0.96f)
                .scaleY(0.96f)
                .setDuration(70)
                .withEndAction(
                        () -> view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(90)
                                .start()
                )
                .start();
    }

    private static void animateIn(
            View view,
            long delay,
            float translationDp
    ) {

        view.setAlpha(0f);

        view.setTranslationY(
                view.getResources()
                        .getDisplayMetrics()
                        .density
                        * translationDp
        );

        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(delay)
                .setDuration(450)
                .setInterpolator(
                        new DecelerateInterpolator(1.4f)
                )
                .start();
    }

    private static int dp(
            Context context,
            float value
    ) {

        return (int) (
                value
                        * context.getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }
}
