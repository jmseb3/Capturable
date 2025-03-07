package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap
import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.CompressFormat
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.write

actual suspend fun CaptureController.captureAsyncAndShare(
    fileName: String,
    imageType: CapturableSaveImageType
) {

}