package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual suspend fun ImageBitmap.encodeToByteArray(
    format: CapturableSaveImageType
): ByteArray = withContext(Dispatchers.Unconfined) {
    val bitmap = this@encodeToByteArray.asSkiaBitmap()
    val imageFormat = when (format) {
        is CapturableSaveImageType.JPEG -> EncodedImageFormat.JPEG
        is CapturableSaveImageType.PNG -> EncodedImageFormat.PNG
        is CapturableSaveImageType.WEBP -> EncodedImageFormat.WEBP
    }
    Image
        .makeFromBitmap(bitmap)
        .encodeToData(imageFormat, format.quality)
        ?.bytes ?: ByteArray(0)
}