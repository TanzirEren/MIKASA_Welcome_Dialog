package com.modmase.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
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
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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
        // IMAGE FALLBACK
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
        // VIDEO
        // =========================

        final VideoView videoView = new VideoView(activity);

        videoView.setBackgroundColor(
                Color.rgb(37, 33, 31)
        );

        videoView.setVisibility(View.GONE);

        FrameLayout.LayoutParams videoParams =
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );

        mediaFrame.addView(videoView, videoParams);

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
        // FIXED VIDEO LOADER
        // =========================================================

        loadVideo(
                activity,
                videoView,
                imageView
        );

        // =========================
        // EXIT
        // =========================

        exitButton.setOnClickListener(v -> {

            press(v);

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

                if (videoView.isPlaying()) {
                    videoView.stopPlayback();
                }

                dialog.dismiss();

            }, 120);
        });

        // =========================
        // DIALOG
        // =========================

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
    // VIDEO LOADER
    // =========================================================

    private static void loadVideo(
            final Activity activity,
            final VideoView videoView,
            final ImageView imageView
    ) {

        try {

            // Check asset exists
            InputStream test =
                    activity.getAssets().open(VIDEO_NAME);

            test.close();

            // Copy asset to cache
            final File videoFile =
                    new File(
                            activity.getCacheDir(),
                            VIDEO_NAME
                    );

            copyAssetToFile(
                    activity,
                    VIDEO_NAME,
                    videoFile
            );

            // Make sure the file exists
            if (!videoFile.exists()
                    || videoFile.length() == 0) {

                return;
            }

            Uri videoUri =
                    Uri.fromFile(videoFile);

            videoView.setVideoURI(videoUri);

            videoView.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(
                                MediaPlayer mp
                        ) {

                            try {

                                mp.setLooping(true);

                                // Keep video muted
                                mp.setVolume(
                                        0f,
                                        0f
                                );

                            } catch (Exception ignored) {
                            }

                            imageView.setVisibility(
                                    View.GONE
                            );

                            videoView.setVisibility(
                                    View.VISIBLE
                            );

                            videoView.setAlpha(0f);
                            videoView.setScaleX(1.05f);
                            videoView.setScaleY(1.05f);

                            videoView.animate()
                                    .alpha(1f)
                                    .scaleX(1.02f)
                                    .scaleY(1.02f)
                                    .setDuration(700)
                                    .setInterpolator(
                                            new DecelerateInterpolator()
                                    )
                                    .start();

                            videoView.start();
                        }
                    }
            );

            videoView.setOnErrorListener(
                    (mp, what, extra) -> {

                        videoView.stopPlayback();

                        videoView.setVisibility(
                                View.GONE
                        );

                        imageView.setVisibility(
                                View.VISIBLE
                        );

                        return true;
                    }
            );

        } catch (Exception e) {

            // Video not found / invalid
            videoView.setVisibility(
                    View.GONE
            );

            imageView.setVisibility(
                    View.VISIBLE
            );
        }
    }

    // =========================================================
    // COPY ASSET
    // =========================================================

    private static void copyAssetToFile(
            Context context,
            String assetName,
            File outputFile
    ) throws Exception {

        InputStream input =
                context.getAssets().open(assetName);

        FileOutputStream output =
                new FileOutputStream(outputFile);

        byte[] buffer = new byte[8192];

        int length;

        while ((length = input.read(buffer)) != -1) {

            output.write(
                    buffer,
                    0,
                    length
            );
        }

        output.flush();
        output.close();
        input.close();
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
