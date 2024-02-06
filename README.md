# Ludwig for Jetpack Compose
### Installation
#### Add JitPack repository (settings.gradle.kts)
```
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}
```

# Morpher
#### Add Gradle dependency (build.gradle.kts (app))
```
dependencies {
    implementation "com.github.baec23.ludwig:morpher:1.0.3"
}
```
[Demo](https://github.com/baec23/ludwig/assets/65561206/a9e49756-aa94-4657-b39c-e48ffb726202)

### Getting Started
#### Create VectorSource from ImageVector or path String
```kotlin
VectorSource.fromImageVector(Icons.Outlined.Star)
VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.androidlogo))
VectorSource.fromPathString("m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z")
```
#### Use AnimatedVector component
When vectorSource changes, AnimatedVector will morph animate the change.
```kotlin
AnimatedVector(
    vectorSource = selectedVectorSource
)
```
#### Full sample
```kotlin
@Composable
fun MorpherSample() {

    //VectorSource can be created from ImageVector
    //    Icons
    //    Imported drawable resources - VectorSource.fromImageVector(ImageVector.vectorResource(R.drawable.imported_vector))
    val vectorSource1 = VectorSource.fromImageVector(Icons.Outlined.Star)
    //VectorSource can also be created from a path string (the 'd' attribute of a <path> element)
    val vectorSource2 =
        VectorSource.fromPathString("m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z")

    var selectedVectorSource by remember { mutableStateOf(vectorSource1) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        //AnimatedVector will automatically animate morph when 'vectorSource' changes
        //    AnimatedVector can be customized with optional params
        //    animationSpec: AnimationSpec<Float>
        //    strokeWidth: Float
        //    strokeColor: Color
        AnimatedVector(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(36.dp),
            vectorSource = selectedVectorSource,
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { selectedVectorSource = vectorSource1 }) {
                Text(text = "Source 1")
            }
            Button(
                modifier = Modifier.weight(1f),
                onClick = { selectedVectorSource = vectorSource2 }) {
                Text(text = "Source 2")
            }
        }
    }
}
```
### Issues
- Only Stroke is supported for now
- Fills will be weird

# UI Components
#### Add Gradle dependency (build.gradle.kts (app))
```
dependencies {
    implementation "com.github.baec23.ludwig:component:1.0.3"
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

