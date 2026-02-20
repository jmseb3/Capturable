package dev.wonddak.capturable.extension

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


actual suspend fun ImageBitmap.encodeToByteArray(
    format: CapturableSaveImageType,
): ByteArray = withContext(Dispatchers.IO) {
    val bitmap = this@encodeToByteArray.asAndroidBitmap()
    val compressFormat = when (format) {
        is CapturableSaveImageType.JPEG -> Bitmap.CompressFormat.JPEG
        is CapturableSaveImageType.PNG -> Bitmap.CompressFormat.PNG
        is CapturableSaveImageType.WEBP -> if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            Bitmap.CompressFormat.WEBP_LOSSLESS
        } else {
            Bitmap.CompressFormat.WEBP
        }
    }
    ByteArrayOutputStream().use { bytes ->
        bitmap.compress(compressFormat, format.quality, bytes)
        bytes.toByteArray()
    }
}