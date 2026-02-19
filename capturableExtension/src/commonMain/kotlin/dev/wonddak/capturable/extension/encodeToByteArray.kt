package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Encodes the [ImageBitmap] into a [ByteArray] using the specified image format.
 *
 * This is an `expect` function, meaning the actual implementation is platform-specific.
 * It's a suspending function because the encoding process can be resource-intensive
 * and should be performed off the main thread.
 *
 * @param format The desired output format for the image, such as JPEG or PNG.
 * @return A [ByteArray] containing the encoded image data.
 */
expect suspend fun ImageBitmap.encodeToByteArray(
    format: CapturableSaveImageType
): ByteArray