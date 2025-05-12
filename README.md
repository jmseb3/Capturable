# Capturable

![Capturable](art/header.png)

🚀 Compose utility library for converting Composable content into ImageBitmap 🖼️.  
_Made with ❤️ for Compose Multiplatform Developers_ 

Thank to [@PatilShreyas](https://www.github.com/PatilShreyas)

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.jmseb3/capturable)
[![Kotlin](https://img.shields.io/badge/kotlin-v2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License](https://img.shields.io/github/license/jmseb3/capturable)](https://opensource.org/license/mit/)

[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.7.3-blue)](https://github.com/JetBrains/compose-multiplatform)
![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)
![badge-desktop](http://img.shields.io/badge/platform-desktop-DB413D.svg?style=flat)
![badge-js](http://img.shields.io/badge/platform-js%2Fwasm-FDD835.svg?style=flat)
## 💡Introduction 

In the previous View system, drawing Bitmap Image from `View` was very straightforward. But that's not the case with Jetpack Compose since it's different in many aspects from previous system. This library helps easy way to achieve the same results.

## 🚀 Implementation

You can check [/composeApp](/composeApp) directory which includes example application for demonstration. 

### Preview

<details>
<summary>Android</summary>
<img src="https://github.com/user-attachments/assets/de83cf54-b789-4acb-89c6-08134a434690" alt="Android Screenshot" width="500"/>
</details>

<details>
<summary>iOS</summary>
<img src="https://github.com/user-attachments/assets/25aa49ae-3019-496c-8515-fb4d6f19c2a0" alt="Android Screenshot" width="500"/>
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

---

<details>
<summary>How To Test sample?</summary>

### Android
To run the application on android device/emulator:  
 - open project in Android Studio and run imported android run configuration

### Desktop
Run the desktop application: `./gradlew :sample:composeApp:run`

### iOS
To run the application on iPhone device/simulator:
- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration

### JS Browser
Run the browser application: `./gradlew :sample:composeApp:jsBrowserDevelopmentRun --continue`

### Wasm Browser
Run the browser application: `./gradlew :sample:composeApp:wasmJsBrowserDevelopmentRun --continue`
</details>

---

### Gradle setup

In `lib.versions.toml`  include this dependency version catalog

```toml
[versions]
capturable = "1.1.0"

[libraries]
capturable = { module = "io.github.jmseb3:capturable", version.ref = "capturable" }
```

```kotlin
dependencies {
    implementation(libs.capturable)
}
```

or In `build.gradle` of app module, include this dependency

```gradle
dependencies {
    implementation("io.github.jmseb3:capturable:1.1.0")
}
```

_You can find latest version and changelogs in the [releases](https://github.com/jmseb3/Capturable/releases)_.

### Usage

#### 1. Setup the controller

To be able to capture Composable content, you need instance of [`CaptureController`](https://jmseb3.github.io/Capturable/-caputerable/dev.wonddak.capturable.controller/-capture-controller/index.html) by which you can decide when to capture the content. You can get the instance as follow.

```kotlin
@Composable
fun TicketScreen() {
    val captureController = rememberCaptureController()
}
```

_[`rememberCaptureController()`](https://jmseb3.github.io/Capturable/-caputerable/dev.wonddak.capturable.controller/remember-capture-controller.html) is a Composable function._

#### 2. Add the content

The component which needs to be captured, a `capturable()` Modifier should be applied on that @Composable component as follows.

```kotlin
@Composable
fun TicketScreen() {
    val captureController = rememberCaptureController()

    // Composable content to be captured.
    // Here, everything inside below Column will be get captured
    Column(modifier = Modifier.capturable(captureController)) {
        MovieTicketContent(...)
    }
}
```

#### 3. Capture the content

To capture the content, use [`CaptureController#captureAsync()`](./docs/capturable/dev.shreyaspatil.capturable.controller/-capture-controller/capture-async.html) as follows. 

```kotlin
// Example: Capture the content when button is clicked
val scope = rememberCoroutineScope()
Button(onClick = {
    // Capture content
    scope.launch {
        val bitmapAsync = captureController.captureAsync()
        try {
            val bitmap = bitmapAsync.await()
            // Do something with `bitmap`.
        } catch (error: Throwable) {
            // Error occurred, do something.
        }
    }
}) { ... }
```

On calling this method, request for capturing the content will be sent and `ImageBitmap` will be 
returned asynchronously. _This method is safe to be called from Main thread._

### Android & iOS
The Platform have [`captureAsyncAndShare`](https://jmseb3.github.io/Capturable/-caputerable/dev.wonddak.capturable/capture-async-and-share.html) and [`captureAsyncAndSave`](https://jmseb3.github.io/Capturable/-caputerable/dev.wonddak.capturable/capture-async-and-save.html)

## 🙋‍♂️ Contribute 

Read [contribution guidelines](CONTRIBUTING.md) for more information regarding contribution.


## 📝 License

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
