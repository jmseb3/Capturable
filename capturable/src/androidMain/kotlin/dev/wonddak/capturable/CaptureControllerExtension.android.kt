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
package dev.wonddak.capturable

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.content.FileProvider
import dev.wonddak.capturable.controller.CaptureController
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Image Type of Android
 */
sealed class ImageType(val suffix: String) {

    /**
     * share type PNG
     * @param quality compress quality(0 ~ 100)
     */
    data class PNG(val quality: Int) : ImageType("png")

    /**
     * share type JPEG
     * @param quality compress quality(0 ~ 100)
     */
    data class JPEG(val quality: Int) : ImageType("jpeg")

    /**
     * File mimeType
     */
    internal val mimeType: String
        get() = "image/$suffix"
}

/**
 * Capture and share Image
 *
 * capture and saveTo Cache Folder then make [Intent.ACTION_SEND] For share
 *
 * also see [CaptureController.captureAsync]
 *
 * Example usage:
 *
 * ```
 *  val captureController = rememberCaptureController()
 *  val uiScope = rememberCoroutineScope()
 *  val context = LocalContext.current
 *
 *  // The content to be captured in to Bitmap
 *  Column(
 *      modifier = Modifier.capturable(captureController),
 *  ) {
 *      // Composable content
 *  }
 *  Button(
 *     onClick = {
 *         scope.launch {
 *             captureController.captureAsyncAndShare(
 *                 context = context
 *             )
 *         }
 *  }) { ... }
 * ```
 * @param[context] Context
 * @param[type]
 *
 * Share Type PNG or JPEG [ImageType]
 *
 * @param[authority]
 *
 * Need For get content[Uri]
 * default value : (com.your.package).fileprovider
 *
 * @param[addOptionChooseIntent]
 *
 * add option if need for [Intent.createChooser]
 * [Custom Actions](https://developer.android.com/training/sharing/send#custom-actions) / [Custom Target](https://developer.android.com/training/sharing/send#adding-custom-targets) / [Excluding Component](https://developer.android.com/training/sharing/send#excluding-specific-targets-by-component)
 *
 *
 * @param[deleteOnExit] clear temp file when exit app
 */
suspend fun CaptureController.captureAsyncAndShare(
    context: Context,
    type: ImageType = ImageType.PNG(100),
    addOptionChooseIntent: (chooseIntent: Intent) -> Unit = {},
    authority: String = context.packageName + ".fileprovider",
    deleteOnExit: Boolean = true
) {
    val bitmap: ImageBitmap = this.captureAsync().await()
    val uri = withContext(Dispatchers.IO) {
        // Make TempFile
        val tempSharedImage = File.createTempFile("capturable", "." + type.suffix)

        BufferedOutputStream(FileOutputStream(tempSharedImage)).use { outputStream ->
            outputStream.write(bitmap.toByteArray(type))
        }
        if (deleteOnExit) {
            // delete cache file on exit
            tempSharedImage.deleteOnExit()
        }
        // Return Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, authority, tempSharedImage)
        } else {
            Uri.fromFile(tempSharedImage)
        }
    }

    // make intent share
    val intentShareImageSend = Intent(Intent.ACTION_SEND)

    val mimeType = type.mimeType
    intentShareImageSend.setType(mimeType)

    // make clipDate for preview
    intentShareImageSend.clipData = ClipData(
        "",
        arrayOf(mimeType),
        ClipData.Item(uri)
    )

    intentShareImageSend.putExtra(Intent.EXTRA_STREAM, uri)
    intentShareImageSend.setFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
    )

    // Any User Option if need custom...

    val chooseIntent = Intent.createChooser(intentShareImageSend, null)
    addOptionChooseIntent(chooseIntent)

    context.startActivity(chooseIntent)
}