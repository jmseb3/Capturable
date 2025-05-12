package dev.wonddak.capturable.extension

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

/**
 * ImageBitmap to ByteArray
 */
actual fun ImageBitmap.toByteArray(type: ImageSaveType): ByteArray {
    val androidBitmap = this.asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    when (type) {
        is ImageSaveType.JPEG -> {
            androidBitmap.compress(Bitmap.CompressFormat.JPEG, type.quality, byteArrayOutputStream)
        }

        is ImageSaveType.PNG -> {
            androidBitmap.compress(Bitmap.CompressFormat.PNG, type.quality, byteArrayOutputStream)
        }
    }

    return byteArrayOutputStream.toByteArray()
}