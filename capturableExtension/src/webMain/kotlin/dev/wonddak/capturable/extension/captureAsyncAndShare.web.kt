package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.extension.annotation.ExperimentalCaptureApi
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import kotlin.js.js

@ExperimentalCaptureApi
suspend fun CaptureController.captureAsyncAndShare(
    fileName: String = "capture_shared",
    imageType: CapturableSaveImageType = CapturableSaveImageType.PNG(100)
) {
    val imageBitmap = this.captureAsync().await()
    val imageBytes = imageBitmap.encodeToByteArray(imageType)
    shareImageOnWeb(
        bytes = imageBytes,
        fileName = imageType.makeFileName(fileName),
        mimeType = imageType.mimeType
    )
}