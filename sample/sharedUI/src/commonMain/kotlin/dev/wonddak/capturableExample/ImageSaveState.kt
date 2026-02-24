package dev.wonddak.capturableExample

import androidx.compose.runtime.Immutable
import dev.wonddak.capturable.extension.CapturableSaveImageType
import dev.wonddak.capturable.extension.CapturableSaveType

@Immutable
data class ImageSaveState(
    val imageType: CapturableSaveImageType = CapturableSaveImageType.PNG(100),
    val saveType : CapturableSaveType = CapturableSaveType.Auto,
    val fileName : String = "Ticket"
)