package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.write

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
suspend fun CaptureController.captureAsyncAndShare(
    fileName: String = "capture_shared",
    imageType: CapturableSaveImageType = CapturableSaveImageType.PNG(100)
) {
    val imageBitmap = this.captureAsync().await()
    val imageBytes = imageBitmap.encodeToByteArray(
        format = when (imageType) {
            is CapturableSaveImageType.JPEG -> {
                ImageFormat.JPEG
            }

            is CapturableSaveImageType.PNG -> {
                ImageFormat.PNG
            }
        },
        quality = imageType.quality
    )

    val compressedBytes = when (imageType) {
        is CapturableSaveImageType.JPEG -> {
            FileKit.compressImage(
                bytes = imageBytes,
                quality = imageType.quality,
                imageFormat = ImageFormat.JPEG
            )
        }

        is CapturableSaveImageType.PNG -> {
            FileKit.compressImage(
                bytes = imageBytes,
                quality = imageType.quality,
                imageFormat = ImageFormat.PNG
            )
        }
    }
    val file = PlatformFile(FileKit.cacheDir, imageType.makeFileName(fileName))
    file.write(bytes = compressedBytes)
}