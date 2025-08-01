package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.saveImageToGallery
import io.github.vinceglb.filekit.write

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
    when (saveType) {
        CapturableSaveType.Gallery -> {
            val file = PlatformFile(FileKit.cacheDir, imageType.makeFileName(fileName))

            file.write(bytes = compressedBytes)
            //saveImageToGallery not return path
            FileKit.saveImageToGallery(file = file)

            //delete Cache File
            file.delete(mustExist = false)
        }

        CapturableSaveType.Auto, CapturableSaveType.Pick -> {
            FileKit.openFileSaver(suggestedName = fileName, extension = imageType.suffix)
                ?.let { file ->
                    file.write(bytes = compressedBytes)
                }
        }
    }
}