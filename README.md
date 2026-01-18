# PERIOD
Android Studio Period Application
PERIODEE is an Android application developed in Kotlin using Jetpack Compose.
The app helps users track their menstrual cycle, understand cycle phases, and receive personalized daily tips related to well-being, sport, and nutrition.

Main Features
User profile setup (name, cycle length, last period date)
Automatic cycle and phase calculation
Personalized daily tips based on cycle phase
Cycle history visualization
Notifications and reminders
Multilingual support (French, English, Spanish)

Architecture
The application follows a clean and modular architecture:
UI Layer: Jetpack Compose screens
Business Logic: dedicated engines (PhaseInfoEngine, TipsEngine)

Data Layer:
DataStore for local user preferences
Firebase Firestore for cycle history
Async processing: Kotlin Coroutines

Technologies
Kotlin
Jetpack Compose & Material 3
DataStore Preferences (local storage)
Firebase Firestore (cloud database)
Google Translate API
Android Studio & Gradle

Data Management
Local storage: DataStore is used to store user preferences and ensure offline availability
Cloud storage: Firebase Firestore stores cycle history data

Multilingual Support
Static UI texts are managed using Android string resources
Dynamic content is translated using Google Translate API
A fallback mechanism ensures the app remains usable if the API is unavailable

Testing
Unit tests: Business logic tested (cycle phase calculation)
Instrumentation tests: Local DataStore persistence tested on device/emulator
Testing focuses on core logic and data reliability

PERIODEE is a complete Android application demonstrating modern development practices, clean architecture, data persistence, external API integration, and testing.