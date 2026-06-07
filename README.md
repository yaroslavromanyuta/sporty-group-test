# SkyCast — Android Weather Forecast App

[![Android CI](https://github.com/yaroslavromanyuta/sporty-group-test/actions/workflows/android-ci.yml/badge.svg?branch=main)](https://github.com/yaroslavromanyuta/sporty-group-test/actions/workflows/android-ci.yml)

SkyCast is a small, production-like Android weather app built for the SkyGroup home
assignment. It shows the current-day and 7-day forecast for your current location or any
city you search for, with explicit handling of loading, error, empty and
location-permission states, plus a **Settings** screen for units and theme. The UI is a
native Jetpack Compose recreation of the **SkyCast design handoff**.

Forecasts are **cached locally**, so the last successful result is still shown (with an
"offline" indicator) when the network is unavailable; **recently searched cities** are
persisted across restarts, and the forecast screen supports **pull-to-refresh**. The app also
**remembers your last forecast source** (current location or a chosen city) and restores it on
launch, so the initial choice screen only appears until you make a first selection.

## Screenshots

### Light Mode
<p align="center">
  <img src="assets/welcome_screen_light.png" width="30%" />
  <img src="assets/main_forecast_screen_light.png" width="30%" />
  <img src="assets/city_search_light.png" width="30%" />
</p>

### Dark Mode
<p align="center">
  <img src="assets/welcome_screen.png" width="30%" />
  <img src="assets/main_forecast_screen.png" width="30%" />
  <img src="assets/city_search_empty.png" width="30%" />
</p>

### Features & States
<p align="center">
  <img src="assets/weekly_forecast_details.png" width="30%" />
  <img src="assets/settings_screen.png" width="30%" />
  <img src="assets/welcome_screen_permission_error.png" width="30%" />
</p>

### Demo
[Watch the app demo](assets/app_demo.mp4)



---

## Requirement coverage

| Requirement                                 | Where                                                                       |
|---------------------------------------------|-----------------------------------------------------------------------------|
| Current-day forecast                        | `CurrentWeatherHero`, `WeatherMetricsGrid`, hourly row                      |
| Weekly forecast                             | `WeeklyForecastList` (7 days from Open-Meteo `daily`)                       |
| Current location forecast                   | `GetCurrentLocationForecastUseCase` + `CurrentLocationProvider`             |
| Choose another city                         | `CitySearchScreen` + Open-Meteo Geocoding                                   |
| Settings (units + theme)                    | `:feature:settings` `SettingsScreen`, persisted with DataStore              |
| Units affect API + UI, reload on change     | `ForecastUnitsMapper` → Open-Meteo params; ViewModel reloads on unit change |
| Theme reacts to setting                     | `MainViewModel` observes `ThemeMode` → `SkyTheme`                           |
| Offline forecast cache                      | `ForecastLocalDataSource` (DataStore + kotlinx.serialization); network-first with cache fallback |
| Recent searched cities                      | `RecentCitiesLocalDataSource` (persisted), shown on empty city search       |
| Remembers last forecast source              | `LastForecastSelectionLocalDataSource` + `Get`/`SaveLastForecastSelectionUseCase`; restored on launch |
| Pull-to-refresh                             | `PullToRefreshBox` on `ForecastScreen` → in-place `OnRefresh`               |
| 100% executable                             | `./gradlew :app:assembleDebug` builds a runnable APK                        |
| No API key committed                        | Open-Meteo needs no key                                                     |
| Kotlin / Coroutines / Compose               | Throughout                                                                  |
| Clean Architecture + feature module         | `:feature:forecast` with data/domain/presentation layers                    |
| Loading / error / empty / permission states | `ForecastUiState` + dedicated composables                                   |

---

## Tech stack

- **Kotlin** 2.3.21, **Coroutines / Flow**
- **Jetpack Compose** (Material 3) + **Navigation Compose**
- **Hilt** for dependency injection
- **Retrofit + OkHttp** with **kotlinx.serialization**
- **Open-Meteo** Forecast & Geocoding APIs (no key required)
- **AGP 9.2.1** (built-in Kotlin), **Gradle 9.4.1**, JDK 21, `compileSdk 37`, `minSdk 24`
- Tests: **JUnit4**, **MockK**, **Turbine**, **kotlinx-coroutines-test**, **MockWebServer**,
  **Compose UI Test**, **Roborazzi** (JVM screenshots)

---

## Architecture

Pragmatic **Clean Architecture + MVVM** with strict per-layer models and explicit mappers:

```
Remote DTO  ──►  Data model  ──►  Domain model  ──►  UI model
 (Retrofit)      (data layer)     (platform-free)    (display-ready strings)
```

Dependency direction: `presentation → domain ← data`. The domain layer has no Android,
Retrofit or Compose dependencies. UI composables never touch repositories or DTOs;
formatting (temperatures, day labels, "Updated 09:30") lives only in presentation mappers.
The shared models (`:core:model`) and settings contracts (`:lib:settings`) are **pure-JVM
Kotlin modules** (they apply `kotlin-jvm`, not the Android library plugin), so an accidental
Android/Retrofit/Compose import there fails to compile — a structural guarantee on top of the
convention, and a slightly faster build.

State is exposed as `StateFlow<ForecastUiState>` / `StateFlow<CitySearchUiState>` from a
single `@HiltViewModel` shared across the forecast nav-graph; one-time effects use a
`SharedFlow`. Screens are stateless (`ForecastScreen`, `CitySearchScreen`) and
driven by hoisted state + action callbacks, each with `@Preview`s for every major state.

### Module structure

```
:app                  Application, MainActivity, root NavHost, theming entry point
:core:model           Pure-JVM, platform-free domain models (Coordinates, City, Forecast, WeatherCondition…)
:core:common          AppResult/AppError, DispatcherProvider, DateTimeProvider, Mapper (+ DI)
:core:designsystem    SkyTheme, tokens, native WeatherIcon/UiIcon, reusable components
:core:network         Shared OkHttp + JSON providers
:core:location        CurrentLocationProvider / CurrentCityNameResolver (+ Android impls, DI)
:lib:settings         Pure-JVM settings contracts: AppSettings, MeasurementSystem,
                      TemperatureUnit, ThemeMode, SettingsRepository, observe/update use cases
:feature:forecast     The weather feature:
  data                DTOs, Retrofit APIs, data models, mappers (incl. ForecastUnitsMapper), repository impl
  data/local          DataStore-backed forecast cache, recent-cities + last-selection stores (kotlinx.serialization)
  domain              repository interface, use cases (models live in :core:model)
  presentation        UI models, mappers, state, ViewModel, screens, components, navigation
  di                  feature Hilt modules (ApiModule, data bindings, presentation bindings)
:feature:settings     Settings implementation: DataStore persistence, repository impl,
                      mappers, Hilt bindings, SettingsViewModel, Compose UI, navigation
```

Module dependency direction (acyclic):

```
:core:model    ◄── :core:designsystem, :core:location, :feature:forecast
:core:common   ◄── :core:location, :feature:forecast
:core:network  ◄── :feature:forecast
:lib:settings  ◄── :feature:forecast, :feature:settings, :app
:feature:forecast, :feature:settings ◄── :app
```

Only `:lib:settings` (the contracts) is shared for settings data — `:feature:forecast`
depends on it, **never** on `:feature:settings`. The DataStore-backed implementation in
`:feature:settings` is wired into the graph by `:app`, so Hilt satisfies the
`SettingsRepository` interface at runtime.

### Settings behavior

- Measurement system (Metric/Imperial), temperature unit (Celsius/Fahrenheit) and theme
  (System/Light/Dark) are persisted with **Preferences DataStore** and exposed as a `Flow`.
- The app theme reacts to the theme setting (`MainViewModel` → `SkyTheme`).
- The forecast request sends the selected units to Open-Meteo (`temperature_unit`,
  `wind_speed_unit`, `precipitation_unit`); changing units **reloads** the forecast.
- Pressure is converted to inHg for Imperial in the presentation mapper (Open-Meteo always
  returns hPa).

### Offline cache, recent cities & last selection

- **Forecast cache** (`:feature:forecast/data/local`): every successful forecast is stored
  in **Preferences DataStore** as `kotlinx.serialization` JSON of a data-layer model (never
  a DTO or UI model), keyed by location identity **plus** the selected temperature and
  measurement units so a unit change never serves data in the wrong units. The repository is
  **network-first**: on a network failure it falls back to the cached forecast, tagged with
  domain metadata (`ForecastSource.CACHE`, `isStale`) so the screen shows a subtle
  "Offline data" / "may be outdated" label. Freshness policy is 30 minutes
  (`ForecastCachePolicy`); stale cache is still shown when offline.
- **Recent cities**: manually selected cities are persisted (deduplicated, most-recent-first,
  capped at 5; current-location is never stored) and surfaced on the city search screen when
  the query is empty. They survive process death and app restarts.
- **Last forecast source**: the source last viewed — current location or a specific city — is
  persisted **separately** from both the cache and recent cities, and restored on launch so the
  initial choice screen is skipped once a choice has been made. A saved *current-location*
  selection is auto-loaded only when permission is still granted; it never triggers a permission
  prompt on start (the use case checks permission without requesting it). A manual city is saved
  on selection; current location is saved only after a successful permission-granted load.
- **Pull-to-refresh**: the forecast content refreshes in place — the existing data stays
  visible with a spinner; a transient failure keeps the current forecast and shows a message
  instead of replacing the screen with an error.

---

## APIs (Open-Meteo)

Chosen because it needs **no API key** (ideal for a public homework repo), supports
coordinate-based forecasts and city geocoding.

- Forecast: `https://api.open-meteo.com/v1/forecast`
- Geocoding: `https://geocoding-api.open-meteo.com/v1/search`

---

## Build, run, test

Requires an Android SDK with **platform 37** installed and a `local.properties` pointing at
it (`sdk.dir=...`). JDK 21 is used by the Gradle toolchain.

```bash
# Build a debug APK
./gradlew :app:assembleDebug

# Full build
./gradlew clean build

# JVM unit + integration tests (mappers, use cases, ViewModel, repository via MockWebServer)
./gradlew :feature:forecast:testDebugUnitTest

# Instrumented Compose UI tests (needs a connected device/emulator)
./gradlew connectedAndroidTest
```

The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

---

## Location permission

On first launch SkyCast shows an explicit choice: use current location or search a city manually.
The runtime location permission request is triggered only after the user taps “Use my location”.

On later launches the previously chosen source is restored automatically — a saved current-location
source is reloaded only if permission is still granted. The permission dialog is **never** shown
without an explicit tap: the restore path checks whether permission is held without requesting it,
and falls back to the choice screen when it isn’t.

---

## Testing

- **Unit:** `WeatherCodeMapperTest`, `ForecastDtoToDataMapperTest`,
  `ForecastDomainToUiMapperTest`, `SearchCitiesUseCaseTest`,
  `GetCurrentLocationForecastUseCaseTest`, `ForecastViewModelTest` (incl. recent-cities,
  last-selection restore and pull-to-refresh behavior), `ForecastCacheKeyTest`,
  `ForecastCacheMapperTest`, `ForecastCachePolicyTest`, `ForecastLocalDataSourceTest`,
  `RecentCitiesLocalDataSourceTest`, `LastForecastSelectionLocalDataSourceTest`,
  `LastForecastSelectionMapperTest`.
- **Integration:** `ForecastRepositoryIntegrationTest` and `CitySearchRepositoryIntegrationTest`
  — real Retrofit + mappers against **MockWebServer** (success, empty search, HTTP error → typed
  failure); `ForecastRepositoryCacheTest` — network-first caching (success saves, failure falls
  back to cache, no cache → error, stale cache still served).
- **Compose UI:** `ForecastScreenTest`, `CitySearchScreenTest` (content/error/permission,
  search results/empty/input).
- **Screenshot:** `DesignSystemScreenshotTest`, `ForecastScreenshotTest`,
  `SettingsScreenshotTest` — JVM (Robolectric + **Roborazzi**) golden-image tests covering
  every reusable component and every screen state. Each composable is captured in **eight
  variants** — light/dark theme × LTR/RTL layout × normal and 1.5× large-font
  **accessibility** scale — so both color themes, RTL, and large-font accessibility are
  asserted at once. The shared harness (`RoborazziScreenshotTest`) lives in
  `:core:designsystem` **test fixtures** and is reused by every module via
  `testImplementation(testFixtures(project(":core:designsystem")))`. Goldens live under
  `<module>/src/test/screenshots/` and are committed.

Run the screenshot tests:

```bash
# Verify the UI against the committed golden images (run on every CI build)
./gradlew verifyRoborazziDebug

# Re-record the goldens after an intentional UI change, then review the diff
./gradlew recordRoborazziDebug
```

> Screenshot tests use Roborazzi on Robolectric (pinned to `@Config(sdk = 34)`) with native
> graphics, so they run entirely on the JVM — no emulator required. The official
> `com.android.compose.screenshot` plugin was evaluated first but its preview discovery does
> not yet work with this AGP 9.2 / Kotlin 2.3 toolchain (it silently finds zero previews),
> so Roborazzi was chosen as the robust, actively-maintained alternative.

---

## Continuous integration

GitHub Actions runs the verification build on every pull request targeting `main`
(`.github/workflows/android-ci.yml`). The workflow triggers on PR `opened`, `synchronize`
(new commits pushed to the PR branch), `reopened` and `ready_for_review` — it does **not**
run on arbitrary branch pushes. On an Ubuntu runner it checks out the repo, sets up **JDK
21** and the **Android SDK**, restores the **Gradle cache**, then runs:

```bash
./gradlew clean build          # compile + lint + unit tests for all modules
./gradlew testDebugUnitTest    # JVM unit + integration tests
./gradlew verifyRoborazziDebug # JVM screenshot regression (no emulator)
```

No secrets or API keys are needed — the app uses the key-free Open-Meteo APIs. Emulator-based
connected tests are intentionally left out of CI (run `./gradlew connectedAndroidTest`
locally against a device/emulator).

**Branch protection:** `main` requires the `Build & verify` check to pass and the branch to
be up to date before a PR can be merged.

**Updating screenshot baselines:** screenshot goldens are generated on the Linux CI runner
to avoid cross-platform rendering differences. After an intentional UI change, comment
`/record-screenshots` on the open PR — the `Record & commit screenshot baselines` job will
re-record the goldens and push them back to the branch automatically. The same job can be
triggered manually from the GitHub Actions UI or via:

```bash
gh workflow run android-ci.yml --ref <branch>
```

---

## Notes / known trade-offs

- The design font (Manrope) is substituted with the platform sans-serif to avoid bundling
  font binaries; weights and sizes match the handoff (notably the light, oversized hero).
- The "Today's details" grid shows the four metrics available from the forecast request
  (feels-like, humidity, wind, pressure); sunrise/visibility from the mock design are
  omitted since they aren’t part of the live request.