package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes

/**
 * Convert ImageBitmap to NSData
 */
@OptIn(ExperimentalForeignApi::class)
internal fun ImageBitmap.toNSData(type: ImageSaveType): NSData? {
    val byteArray = this.toByteArray(type)
    return byteArray.usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), byteArray.size.toULong())
    }
}