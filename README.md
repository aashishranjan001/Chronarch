# Chronarch

**A focus timer that turns deep work into a reward system.**

Run timed focus sessions, earn Focus Points for every session you complete, and cash those points in on rewards you define yourself — a movie night, a cheat meal, twenty guilt-free minutes of Instagram. Chronarch gamifies productivity instead of just tracking it.

![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-27-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

[![Google Play](https://img.shields.io/badge/Google%20Play-Closed%20Testing-414141?logo=google-play&logoColor=white)](https://play.google.com/apps/testing/com.aashish.chronarch)

## Overview

Chronarch is a native Android app for people who want their focus time to feel like progress. Start a **Short (30 min)** or **Long (60 min)** session and a persistent notification tracks it in the background. Finish a session and you earn **Focus Points**, with bonus points for keeping a daily streak alive. Spend those points in the **Redemption Corner** on rewards you've set up yourself, and review how your week went with a stats overview and a full session/transaction history.

## Screenshots

<table>
  <tr>
    <td align="center"><b>Home</b></td>
    <td align="center"><b>Week Overview</b></td>
    <td align="center"><b>Redemption Corner</b></td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/home.jpg" width="220"/></td>
    <td><img src="docs/screenshots/week-overview-1.jpg" width="220"/></td>
    <td><img src="docs/screenshots/redemption-corner.jpg" width="220"/></td>
  </tr>
  <tr>
    <td align="center"><b>History — Sessions</b></td>
    <td align="center"><b>History — Transactions</b></td>
    <td align="center"><b>Week Overview — Breakdown</b></td>
  </tr>
  <tr>
    <td><img src="docs/screenshots/history-sessions.jpg" width="220"/></td>
    <td><img src="docs/screenshots/history-transactions.jpg" width="220"/></td>
    <td><img src="docs/screenshots/week-overview-2.jpg" width="220"/></td>
  </tr>
</table>

## Features

- **Focus timers** — Short (30 min) and Long (60 min) sessions with a live countdown ring, run via a foreground service so a session keeps going even if the app is backgrounded.
- **Timer notifications** — a persistent ongoing notification shows remaining time and end time with a one-tap Stop action, followed by a completion notification when the session finishes; tapping either deep-links straight back into the app.
- **Focus Points economy** — earn points for every completed session, plus bonus points for maintaining a streak.
- **Redemption Corner** — define your own rewards and their point cost, then redeem earned points against them.
- **Weekly overview** — a bar chart of point earnings across the week, session counts by duration, and a completed-vs-cancelled breakdown.
- **History with filters** — browse past sessions (by duration/status) and the full points transaction ledger (by type and date range).
- **Streaks** — build a daily completion streak; stopping a session early resets your progress, keeping the incentive honest.
- **Adaptive layout** — a bottom navigation bar on phones that switches to a navigation rail on tablets and foldables, driven by window size class.
- **Material 3 theming** with dynamic color and a native splash screen.

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 (incl. adaptive layouts) |
| Architecture | Clean Architecture / MVVM, organized per feature |
| Dependency injection | Hilt |
| Persistence | Room |
| Concurrency | Kotlin Coroutines + Flow |
| Navigation | Navigation Compose |
| Build | Gradle Kotlin DSL, AGP 9.2.1, Kotlin 2.4.10, KSP |

## Architecture

The app is organized as one Gradle module split into self-contained feature packages, each following the same `data` → `domain` → `presentation` layering:

```
app/src/main/java/com/aashish/chronarch/
├── MainActivity.kt, ChronarchApplication.kt
├── common/          # shared models, repositories, UI components, theme
├── data/local/database/   # Room database
├── di/              # Hilt modules
├── navigation/      # nav graph, bottom bar / nav rail scaffolds
├── home/            # focus timer, notification, foreground service
├── history/         # session & transaction history
├── redemption/      # reward setup and redemption
└── weekoverview/    # weekly stats and graphs
```

Each feature package keeps its `domain` layer (models, repository interfaces, use cases) independent of Compose, with `presentation` holding the ViewModel, UI state, and screen composables — keeping business logic testable and features easy to reason about in isolation.

## Getting started

Chronarch is currently in **closed testing** on Google Play:
1. Join the [chronarch-closed-testers](https://groups.google.com/g/chronarch-closed-testers) Google Group.
2. Opt in to testing via the [Play Store testing link](https://play.google.com/apps/testing/com.aashish.chronarch).

To build it from source instead:

**Requirements**
- Android Studio (recent stable)
- JDK 11
- Android SDK: compileSdk / targetSdk 37, minSdk 27

**Run it**
```bash
git clone https://github.com/aashishranjan001/Chronarch.git
cd Chronarch
```
Open the project in Android Studio, let Gradle sync, then run the `app` configuration on an emulator or device. No API keys or external services are required — everything runs and persists locally via Room.

## Project info

- **Package:** `com.aashish.chronarch`
- **Version:** 1.2.0 (build 4)

## License

Licensed under the [MIT License](LICENSE).
