import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import dev.wonddak.capturable.ImageType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes

/**
 * ImageBitmap to ByteArray
 */
fun ImageBitmap.toByteArray(
    type: ImageType
): ByteArray {
    val skiaBitmap = this.asSkiaBitmap()
    val image: Image = Image.makeFromBitmap(skiaBitmap)

    val imageData = when (type) {
        is ImageType.JPEG -> image.encodeToData(
            format = EncodedImageFormat.JPEG,
            quality = type.quality
        )

        is ImageType.PNG -> image.encodeToData(
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
internal fun ImageBitmap.toNSData(type: ImageType): NSData? {
    val byteArray = this.toByteArray(type)
    return byteArray.usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), byteArray.size.toULong())
    }
}