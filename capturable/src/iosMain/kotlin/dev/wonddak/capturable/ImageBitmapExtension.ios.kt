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
fun ImageBitmap.toByteArray(type: ImageType): ByteArray {
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