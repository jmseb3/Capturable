package dev.wonddak.capturable

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

/**
 * ImageBitmap to ByteArray
 */
fun ImageBitmap.toByteArray(
    type: ImageType,
): ByteArray {
    val androidBitmap = this.asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    when (type) {
        is ImageType.JPEG -> {
            androidBitmap.compress(Bitmap.CompressFormat.JPEG, type.quality, byteArrayOutputStream)
        }
        is ImageType.PNG -> {
            androidBitmap.compress(Bitmap.CompressFormat.PNG, type.quality, byteArrayOutputStream)
        }
    }

    return byteArrayOutputStream.toByteArray()
}