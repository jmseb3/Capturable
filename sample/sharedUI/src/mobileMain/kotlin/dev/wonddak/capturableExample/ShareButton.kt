package dev.wonddak.capturableExample

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.controller.rememberCaptureController
import dev.wonddak.capturable.extension.CapturableSaveImageType
import dev.wonddak.capturable.extension.captureAsyncAndShare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun ShareButton(
    imageSaveState: ImageSaveState,
    scope: CoroutineScope,
    controller: CaptureController
) {
    Button(
        onClick = {
            scope.launch {
                runCatching {
                    controller.captureAsyncAndShare(
                        fileName = imageSaveState.fileName,
                        imageType = imageSaveState.imageType,
                    )
                }.onSuccess {

                }.onFailure {
                    it.printStackTrace()
                }
            }
        }
    ) {
        Text("Share Ticket")
    }
}

@Preview
@Composable
private fun ShareButtonPreview() {
    ShareButton(
        imageSaveState = ImageSaveState(),
        scope = rememberCoroutineScope(),
        controller = rememberCaptureController()
    )
}