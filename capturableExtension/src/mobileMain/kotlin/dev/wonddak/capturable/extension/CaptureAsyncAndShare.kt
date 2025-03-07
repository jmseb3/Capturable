package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController

expect suspend fun CaptureController.captureAsyncAndShare(
    fileName: String = "capture_shared",
    imageType: CapturableSaveImageType = CapturableSaveImageType.PNG(100)
)