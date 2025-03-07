package dev.wonddak.capturable.extension

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

/**
 * ImageBitmap to ByteArray bt type
 * @param[type] image Type
 */
fun ImageBitmap.toByteArray(type: CapturableSaveImageType): ByteArray {
    val androidBitmap = this.asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    when (type) {
        is CapturableSaveImageType.JPEG -> {
            androidBitmap.compress(Bitmap.CompressFormat.JPEG, type.quality, byteArrayOutputStream)
        }

        is CapturableSaveImageType.PNG -> {
            androidBitmap.compress(Bitmap.CompressFormat.PNG, type.quality, byteArrayOutputStream)
        }
    }
    return byteArrayOutputStream.toByteArray()
}