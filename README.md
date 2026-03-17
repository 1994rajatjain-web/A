# AI Old Photo Restorer & Colorizer (Android, Java)

Production-style Android Studio project in **Java** for restoring old photos with an AI backend.

## Features

- Upload image from **Gallery** or **Camera**
- Send image to backend using **Retrofit** (`POST /restore`)
- AI pipeline toggles included in payload:
  - B/W to color
  - Face restoration
  - Scratch removal
  - HD upscaling
- Processing screen with loading indicator
- Before vs After result screen with slider
- Download restored image
- Share restored image
- Rewarded ad gate before restore (**AdMob**)
- Premium billing scaffolding (**Google Play Billing**)

## Project Structure

```text
A/
├── build.gradle
├── settings.gradle
├── gradle.properties
└── app/
    ├── build.gradle
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/aioldphotorestorer/
        │   ├── App.java
        │   ├── ads/AdManager.java
        │   ├── api/{ApiClient,RestoreApiService}.java
        │   ├── billing/BillingManager.java
        │   ├── model/RestoreResponse.java
        │   ├── repository/RestoreRepository.java
        │   ├── ui/{SplashActivity,HomeActivity,UploadPhotoActivity,ProcessingActivity,ResultActivity,BeforeAfterImageView}.java
        │   ├── adapter/FeatureAdapter.java
        │   └── util/FileUtils.java
        └── res/
            ├── layout/*.xml
            ├── values/{colors,strings,themes,dimens}.xml
            ├── drawable/*.xml
            └── xml/file_paths.xml
```

## Required Setup

1. Open in **Android Studio Hedgehog+**.
2. Set backend URL in `app/build.gradle`:
   - `buildConfigField "String", "BASE_URL", '"https://your-backend-domain.com/api/"'`
3. Replace AdMob test IDs with your production IDs.
4. Configure Play Billing product:
   - `premium_unlimited_restores`
5. Ensure backend endpoint accepts multipart:
   - `POST /restore`
   - form-data: `image`, `colorize`, `faceRestore`, `scratchRemoval`, `upscaleHd`
   - response JSON example:
     ```json
     {
       "requestId": "abc123",
       "restoredImageUrl": "https://cdn.example.com/restored/photo.jpg"
     }
     ```

## Run

1. Connect emulator/device.
2. Build & run from Android Studio.
3. Flow:
   - Splash → Home → Upload → Processing → Result.

## Notes for Production Hardening

- Add authentication and signed uploads.
- Add WorkManager for long-running processing and retries.
- Replace in-memory premium state with encrypted persistent state.
- Add analytics/crash reporting.
- Add runtime permission handling UI for camera/gallery on old Android versions.
