# MODMASE MIKASA Dialog v3

Native Java Android dialog with a looping welcome video.

## Add the welcome video

Place your video at:

```text
app/src/main/assets/wlc_video.mp4
```

Recommended: MP4, H.264 video, 16:9. Audio is muted by the app and the video loops automatically.

If `wlc_video.mp4` is missing or cannot be played, the dialog falls back to `mikasa.png`.

## Build

Use GitHub Actions → **Build MODMASE APK** → **Run workflow**.

The workflow verifies that the APK contains exactly one `classes.dex`.
