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

suspend fun CaptureController.captureAsyncAndSave(
    fileName: String,
    type: ImageSaveType
) = runCatching {
    val bitmap: ImageBitmap = this.captureAsync().await()
    val downloadDir = when (PlatformUtil.current) {
        Platform.Linux, Platform.MacOS -> System.getenv("HOME")?.let { Paths.get(it, "Downloads") }
        Platform.Windows -> System.getenv("USERPROFILE")?.let { Paths.get(it, "Downloads") }
    } ?: throw IllegalStateException("Unable to resolve download directory")

    // Ensure directory exists
    Files.createDirectories(downloadDir)

    // Convert image to byte array
    val bytes = bitmap.toByteArray(type)

    val outputPath = downloadDir.resolve(type.makeFileName(fileName))
    Files.write(outputPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
}

private fun getEnv(key: String): String {
    return System.getenv(key)
        ?: throw IllegalStateException("Environment variable $key not found.")
}

