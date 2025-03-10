package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController

/**
 * Capture and share Image
 *
 * also see [CaptureController.captureAsync]
 *
 * - Android : Saves to the device’s Cache directory and makes [Intent.ACTION_SEND](https://developer.android.com/training/sharing/send#send-binary-content)
 * - iOS : Saves to the device’s Cache directory and open [UIActivityViewController](https://developer.apple.com/documentation/uikit/uiactivityviewcontroller)
 * - JVM : Not supported
 * - JS/WASM : Not supported
 *
 * Example usage:
 *
 * ```
 *  val captureController = rememberCaptureController()
 *  val uiScope = rememberCoroutineScope()
 *
 *  // The content to be captured in to Bitmap
 *  Column(
 *      modifier = Modifier.capturable(captureController),
 *  ) {
 *      // Composable content
 *  }
 *  Button(
 *      onClick = {
 *          scope.launch {
 *              captureController.captureAsyncAndShare(
 *                  fileName = "Ticket",
 *                  imageType = CapturableSaveImageType.PNG(100),
 *               )
 *          }
 *  }) { ... }
 * ```

 *
 * @param[fileName]
 *
 * Do not add an extension with a dot ('.'), the appropriate extension will be automatically applied based on the [ImageType].
 *
 * @param[imageType]
 *
 * Share Type PNG or JPEG [CapturableSaveImageType]
 */
expect suspend fun CaptureController.captureAsyncAndShare(
    fileName: String = "capture_shared",
    imageType: CapturableSaveImageType = CapturableSaveImageType.PNG(100)
)