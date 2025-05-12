package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

/**
 * ImageBitmap to ByteArray
 */
actual fun ImageBitmap.toByteArray(type: ImageSaveType): ByteArray {
    val skiaImage = Image.makeFromBitmap(this.asSkiaBitmap())
    val format = when (type) {
        is ImageSaveType.PNG -> EncodedImageFormat.PNG
        is ImageSaveType.JPEG -> EncodedImageFormat.JPEG
    }
    val quality = when (type) {
        is ImageSaveType.PNG -> type.quality
        is ImageSaveType.JPEG -> type.quality
    }
    val data = skiaImage.encodeToData(format, quality)
    return data?.bytes ?: ByteArray(0)
}