package dev.wonddak.capturableExample

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.controller.rememberCaptureController
import dev.wonddak.capturable.extension.CapturableSaveImageType
import dev.wonddak.capturable.extension.captureAsyncAndShare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ShareButton(
    scope: CoroutineScope,
    controller: CaptureController
) {
    Button(
        onClick = {
            scope.launch {
                runCatching {
                    controller.captureAsyncAndShare(
                        fileName = "Ticket",
                        imageType = CapturableSaveImageType.PNG(100),
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
        scope = rememberCoroutineScope(),
        controller = rememberCaptureController()
    )
}