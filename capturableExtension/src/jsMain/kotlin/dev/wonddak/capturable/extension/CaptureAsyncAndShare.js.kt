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
@file:Suppress("UnsafeCastFromDynamic")

package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import io.github.vinceglb.filekit.download
import kotlin.js.Promise
import kotlinx.browser.window
import kotlinx.coroutines.await

actual suspend fun CaptureController.captureAsyncAndShare(
    fileName: String,
    imageType: CapturableSaveImageType
) {
    val imageBitmap = this.captureAsync().await()
    val imageBytes = imageBitmap.encodeToByteArray(imageType)
    val fullFileName = imageType.makeFileName(fileName)
    val file = createShareFile(imageBytes, fullFileName, imageType.mimeType)
    val navigator = window.navigator.asDynamic()
    val shareData = js("{}")

    shareData.files = arrayOf(file)
    shareData.title = fileName

    val canShareFiles = navigator.share != undefined &&
        (
            navigator.canShare == undefined ||
                navigator.canShare(js("{ files: [file] }")) as Boolean
            )

    if (canShareFiles) {
        runCatching {
            (navigator.share(shareData) as Promise<dynamic>).await()
        }.onSuccess {
            return
        }
    }

    FileKit.download(imageBytes, fullFileName)
}

private fun createShareFile(imageBytes: ByteArray, fileName: String, mimeType: String): dynamic {
    val blob = js("new Blob([imageBytes], { type: mimeType })")
    return js("new File([blob], fileName, { type: mimeType })")
}