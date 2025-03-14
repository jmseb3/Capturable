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
import androidx.compose.ui.window.ComposeUIViewController
import dev.wonddak.capturable.ImageType
import dev.wonddak.capturable.captureAsyncAndSave
import dev.wonddak.capturable.captureAsyncAndShare
import kotlinx.coroutines.launch
import platform.UIKit.UIViewController

fun mainViewController(): UIViewController = ComposeUIViewController {
    App(
        otherContent = { scope, captureController ->
            Button(
                onClick = {
                    scope.launch {
                        captureController.captureAsyncAndShare(
                            addOptionUIActivityViewController = { shareVc ->
                            }
                        )
                    }
                }
            ) {
                Text("Share Ticket Image")
            }

            Button(
                onClick = {
                    scope.launch {
                        captureController.captureAsyncAndSave(
                            fileName = "Ticket",
                            type = ImageType.PNG(100)
                        )
                    }
                }
            ) {
                Text("Save Ticket")
            }
        }
    )
}