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
package dev.wonddak.capturable

import androidx.compose.ui.graphics.ImageBitmap
import dev.wonddak.capturable.controller.CaptureController
import dev.wonddak.capturable.extension.ImageSaveType
import dev.wonddak.capturable.extension.toByteArray
import dev.wonddak.capturable.util.Platform
import dev.wonddak.capturable.util.PlatformUtil
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

suspend fun CaptureController.captureAsyncAndSave(fileName: String, type: ImageSaveType) =
    runCatching {
        val bitmap: ImageBitmap = this.captureAsync().await()
        val downloadDir = when (PlatformUtil.current) {
            Platform.Linux, Platform.MacOS ->
                System.getenv("HOME")?.let {
                    Paths.get(it, "Downloads")
                }

            Platform.Windows ->
                System.getenv("USERPROFILE")?.let {
                    Paths.get(it, "Downloads")
                }
        } ?: throw IllegalStateException("Unable to resolve download directory")

        // Ensure directory exists
        Files.createDirectories(downloadDir)

        // Convert image to byte array
        val bytes = bitmap.toByteArray(type)

        val outputPath = downloadDir.resolve(type.makeFileName(fileName))
        Files.write(
            outputPath,
            bytes,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }

private fun getEnv(key: String): String = System.getenv(key)
    ?: throw IllegalStateException("Environment variable $key not found.")