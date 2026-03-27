/*
* MIT License
*
* Copyright (c) 2022 Shreyas Patil
* Copyright (c) 2024 Wonddak
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/
package dev.wonddak.capturableExample

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.controller.rememberCaptureController
import dev.wonddak.capturable.extension.captureAsyncAndShare
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShareButton(
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
                        imageType = imageSaveState.imageType
                    )
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