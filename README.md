# MODMASE MIKASA Dialog v3

Native Java Android dialog with a looping welcome video.

## Add the welcome video

Place your video at:

```text
app/src/main/assets/wlc_video.mp4
```

Recommended: MP4, H.264 video, 16:9. Audio is muted by the app and the video loops automatically.

If no video is found or it cannot be played, the dialog falls back to `mikasa.png`.

### Adding the video with MT Manager

Put the file in the APK's `assets/` folder and name it exactly `wlc_video.mp4`.
If the name differs, the dialog automatically uses the first video file it finds in `assets/`
(`.mp4 .m4v .mov .3gp .webm .mkv`, up to 2 folders deep).

The dialog now uses a `TextureView` + `MediaPlayer` (the old `VideoView` stayed hidden and its
surface was never created, so the video never started). Copy the **whole** `classes.dex`
(or every `MIKASA*.smali`, including `MIKASA$VideoController`) into the target APK.

## Build

Use GitHub Actions → **Build MODMASE APK** → **Run workflow**.

The workflow verifies that the APK contains exactly one `classes.dex`.
