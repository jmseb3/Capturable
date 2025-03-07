package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes

/**
 * ImageBitmap to ByteArray bt type
 * @param[type] image Type
 */
actual fun ImageBitmap.toByteArray(type: CapturableSaveImageType): ByteArray {
    val skiaBitmap = this.asSkiaBitmap()
    val image: Image = Image.makeFromBitmap(skiaBitmap)

    val imageData = when (type) {
        is CapturableSaveImageType.JPEG -> image.encodeToData(
            format = EncodedImageFormat.JPEG,
            quality = type.quality
        )

        is CapturableSaveImageType.PNG -> image.encodeToData(
            format = EncodedImageFormat.PNG,
            quality = type.quality
        )
    }

    return imageData?.bytes ?: ByteArray(0)
}

/**
 * Convert ImageBitmap to NSData
 */
@OptIn(ExperimentalForeignApi::class)
internal fun ImageBitmap.toNSData(type: CapturableSaveImageType): NSData? {
    val byteArray = this.toByteArray(type)
    return byteArray.usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), byteArray.size.toULong())
    }
}