package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.download

/**
 * Capture and save Image To Gallery
 *
 * also see [CaptureController.captureAsync]
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
 *              captureController.captureAsyncAndSave(
 *                  fileName = "Ticket",
 *                  imageType = CapturableSaveImageType.PNG(100),
 *                  saveType = CapturableSaveType.Auto
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
 *
 * @param[saveType]
 *
 * Share Type Auto,Pick and Gallery [CapturableSaveType]
 */
actual suspend fun CaptureController.captureAsyncAndSave(
    fileName: String,
    imageType: CapturableSaveImageType,
    saveType: CapturableSaveType
) {
    val imageBitmap = this.captureAsync().await()
    val imageBytes = imageBitmap.toByteArray(imageType)

    when (saveType) {
        CapturableSaveType.Auto, CapturableSaveType.Pick, CapturableSaveType.Gallery -> {
            FileKit.download(imageBytes, imageType.makeFileName(fileName))
        }
    }
}