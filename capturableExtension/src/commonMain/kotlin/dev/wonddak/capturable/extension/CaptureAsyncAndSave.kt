package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController

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
expect suspend fun CaptureController.captureAsyncAndSave(
    fileName: String = "capture_shared",
    imageType: CapturableSaveImageType = CapturableSaveImageType.PNG(100),
    saveType: CapturableSaveType = CapturableSaveType.Auto
)


sealed interface CapturableSaveType {

    /**
     * Automatically uses optimized values for each platform.
     */
    data object Auto : CapturableSaveType

    /**
     * Pick Directory And Save
     */
    data object Pick : CapturableSaveType

    /**
     * Auto Save For Gallery
     */
    data object Gallery : CapturableSaveType
}

