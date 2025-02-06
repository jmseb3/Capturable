/*
* MIT License
*
* Copyright (c) 2025 Shreyas Patil
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
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import dev.wonddak.capturable.controller.CaptureController
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Capture and share Image
 *
 * capture and saveTo Cache Folder then make [Intent.ACTION_SEND] For share
 *
 * also see [CaptureController.captureAsync]
 *
 * @param[context] Context
 * @param[authority] Need For get [Uri]
 * default value : (com.your.package).fileprovider
 *
 *
 * @param[deleteOnExit] clear temp file when exit app
 */
suspend fun CaptureController.captureAsyncAndShare(
    context: Context,
    authority: String = context.packageName + ".fileprovider",
    deleteOnExit:Boolean = true,
) {
    val bitmap: ImageBitmap = this@captureAsyncAndShare.captureAsync().await()
    val uri = withContext(Dispatchers.IO) {
        // Convert to AndroidBitmap
        val androidBitmap = bitmap.asAndroidBitmap()

        // Make TempFile
        val tempSharedImage = File.createTempFile("capturable", ".png")

        // Save To PNG
        val outputStream = FileOutputStream(tempSharedImage)
        androidBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()

        if (deleteOnExit) {
            //delete cache file on exit
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
    val intentShareFile = Intent(Intent.ACTION_SEND)

    val mimeType = "image/png"
    intentShareFile.setType(mimeType)

    //make clipDate for preview
    intentShareFile.clipData = ClipData(
        "",
        arrayOf(mimeType),
        ClipData.Item(uri)
    )

    intentShareFile.putExtra(Intent.EXTRA_STREAM, uri)
    intentShareFile.setFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
    )
    context.startActivity(Intent.createChooser(intentShareFile, "123"))
}