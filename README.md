# Ludwig - A Jetpack Compose UI Library
## Installation
### Add JitPack.io repository (settings.gradle.kts)
```
repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}
```
### Add Gradle dependency (build.gradle.kts (app))
```
dependencies {
    implementation "com.github.baec23:ludwig:1.0.1"
}
```

## Component Showcase
### DisplaySection / ExpandableDisplaySection / PreferenceSection
Static and expandable display sections to organize content - examples shown in demos for other components

### TimePicker
iOS style time pickers

[timePickers.webm](https://github.com/baec23/ludwig/assets/65561206/f4d11d3f-1545-4ecb-8fa1-aac4c2554cc1)

### DatePicker
Animated Material3 date picker

[datePicker.webm](https://github.com/baec23/ludwig/assets/65561206/869051f0-cf0a-4b8f-9127-e86e30c26557)

### InputFields
Customizable animated input fields with optional error states

[inputField.webm](https://github.com/baec23/ludwig/assets/65561206/769c1a66-49b7-4f12-918c-879080a45023)

### Buttons
Customizable animated buttons with state

[statefulButton.webm](https://github.com/baec23/ludwig/assets/65561206/ed701dd8-7070-4f72-96ec-e9ffd4c2fac3)

### FadingLazy
LazyRow / LazyColumn / LazyHorizontalGrid / LazyVerticalGrid with customizable fading edges to represent scrollability

[fadingLazy.webm](https://github.com/baec23/ludwig/assets/65561206/13d63f59-767f-4602-a882-b0a8e7f1a1e3)

