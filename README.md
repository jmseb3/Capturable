# Capturable

![Capturable](art/header.png)

Compose Multiplatform utility library for capturing Composable content as `ImageBitmap`.

Thanks to [@PatilShreyas](https://www.github.com/PatilShreyas) for the original inspiration.

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.jmseb3/capturable)
[![Kotlin](https://img.shields.io/badge/kotlin-v2.3.10-blue.svg?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.10.2-blue)](https://github.com/JetBrains/compose-multiplatform)
[![License](https://img.shields.io/github/license/jmseb3/capturable)](https://opensource.org/license/mit/)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)
![badge-js](http://img.shields.io/badge/platform-js%2Fwasm-FDD835.svg?style=flat)

## Introduction

Capturable makes it easy to capture Compose UI content into bitmap images across platforms.

## Modules

- `io.github.jmseb3:capturable`: core capture API (`CaptureController`, `Modifier.capturable`)
- `io.github.jmseb3:capturableExtension`: optional save/share helpers built on top of FileKit

## Installation

### Version catalog (`libs.versions.toml`)

```toml
[versions]
capturable = "2.0.2"

[libraries]
capturable = { module = "io.github.jmseb3:capturable", version.ref = "capturable" }
capturable-extension = { module = "io.github.jmseb3:capturableExtension", version.ref = "capturable" }
```

```kotlin
dependencies {
    implementation(libs.capturable)
    implementation(libs.capturable.extension)
}
```

### Direct dependency

```kotlin
dependencies {
    implementation("io.github.jmseb3:capturable:2.0.2")
    implementation("io.github.jmseb3:capturableExtension:2.0.2")
}
```

Latest versions and changelog: [Releases](https://github.com/jmseb3/Capturable/releases)

## Quick Start

### 1. Create a controller

```kotlin
@Composable
fun TicketScreen() {
    val captureController = rememberCaptureController()
}
```

### 2. Mark target content as capturable

```kotlin
@Composable
fun TicketScreen() {
    val captureController = rememberCaptureController()

    Column(modifier = Modifier.capturable(captureController)) {
        MovieTicketContent(...)
    }
}
```

### 3. Capture asynchronously

```kotlin
val scope = rememberCoroutineScope()

Button(onClick = {
    scope.launch {
        runCatching { captureController.captureAsync().await() }
            .onSuccess { bitmap -> /* use bitmap */ }
            .onFailure { error -> error.printStackTrace() }
    }
}) { ... }
```

## Extension (Save/Share)

If you need `captureAsyncAndSave` or `captureAsyncAndShare`, use `capturableExtension`.

It depends on [FileKit](https://github.com/vinceglb/FileKit). Follow the [FileKit setup guide](https://filekit.mintlify.app/core/setup).

Android initialization example:

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FileKit.init(this)
    }
}
```

| Platform | captureAsyncAndSave | captureAsyncAndShare |
|:--------:|:-------------------:|:--------------------:|
| Android  |          ✅          |          ✅           |
| iOS      |          ✅          |          ✅           |
| JVM      |          ✅          |          ❎           |
| JS       |          ✅          |          ❎           |
| WASM     |          ✅          |          ❎           |

## Sample App

Example app source lives in [`sample/`](sample).

- Desktop: `./gradlew :sample:desktopApp:run`
- JS browser: `./gradlew :sample:webApp:jsBrowserDevelopmentRun --continue`
- WASM browser: `./gradlew :sample:webApp:wasmJsBrowserDevelopmentRun --continue`
- Android: run `sample:androidApp` from Android Studio
- iOS: open `sample/iosApp/iosApp.xcodeproj` in Xcode and run

## Preview

<details>
<summary>Android</summary>
<img src="https://github.com/user-attachments/assets/de83cf54-b789-4acb-89c6-08134a434690" alt="Android Screenshot" width="500"/>
</details>

<details>
<summary>iOS</summary>
<img src="https://github.com/user-attachments/assets/25aa49ae-3019-496c-8515-fb4d6f19c2a0" alt="iOS Screenshot" width="500"/>
</details>

<details>
<summary>Desktop</summary>

![Desktop](https://github.com/user-attachments/assets/ca8040ae-e22f-4a1f-8d07-14da4685aa83)

</details>

<details>
<summary>JS</summary>

![JS](https://github.com/user-attachments/assets/0ce4ce6c-417b-4689-9539-107c31750e2f)

</details>

<details>
<summary>WASM</summary>

![WASM](https://github.com/user-attachments/assets/a3f1d21b-f53f-4b8d-8dc0-77f00fc794e2)

</details>

## Contribute

Read [contribution guidelines](CONTRIBUTING.md) for details.

## License

```
MIT License

Copyright (c) 2022 Shreyas Patil
Copyright (c) 2024 WonDDak

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
