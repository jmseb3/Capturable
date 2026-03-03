package dev.wonddak.capturableExample

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.extension.annotation.ExperimentalCaptureApi
import dev.wonddak.capturable.extension.captureAsyncAndShare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WebApp() {
    App(
        otherContent = { imageSaveState, scope, controller ->
            ShareButton(imageSaveState, scope, controller)
        }
    )
}

@OptIn(ExperimentalCaptureApi::class)
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
                        fileName = imageSaveState.imageType.makeFileName(imageSaveState.fileName),
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