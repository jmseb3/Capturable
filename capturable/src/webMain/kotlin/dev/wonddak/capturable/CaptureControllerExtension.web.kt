package dev.wonddak.capturable

import androidx.compose.ui.graphics.ImageBitmap
import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.extension.ImageSaveType
import dev.wonddak.capturable.extension.toByteArray

expect fun download(
    byteArray: ByteArray,
    fileName: String
)

suspend fun CaptureController.captureAsyncAndSave(
    fileName: String,
    type: ImageSaveType
) = runCatching {
    val bitmap: ImageBitmap = this.captureAsync().await()
    download(bitmap.toByteArray(type), type.makeFileName(fileName))
}