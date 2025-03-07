package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController

actual suspend fun CaptureController.captureAsyncAndShare(
    fileName: String,
    imageType: CapturableSaveImageType
) {
}