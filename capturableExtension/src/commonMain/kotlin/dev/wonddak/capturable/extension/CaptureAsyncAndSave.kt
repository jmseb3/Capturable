/*
* MIT License
*
* Copyright (c) 2022 Shreyas Patil
* Copyright (c) 2024 Wonddak
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/
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

/**
 * When Save Type
 */
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
     *
     * [FileKit#saving images to gallery](https://filekit.mintlify.app/core/image-utils#saving-images-to-gallery)
     *
     * - Android : Saves to the device’s Pictures directory and makes it visible in the gallery app
     * - iOS : Saves to the device’s Camera Roll
     * - JVM : Saves to the user’s Pictures directory
     * - JS/WASM : Not supported (no-op) >> Auto Convert to "Pick"
     *
     */
    data object Gallery : CapturableSaveType
}