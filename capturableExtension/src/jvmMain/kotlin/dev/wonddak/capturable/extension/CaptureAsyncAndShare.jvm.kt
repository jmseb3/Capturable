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
package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.dialogs.compose.util.encodeToByteArray
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.File
import java.nio.file.Files
import java.util.Locale

actual suspend fun CaptureController.captureAsyncAndShare(
    fileName: String,
    imageType: CapturableSaveImageType
) {
    val imageBitmap = this.captureAsync().await()
    val imageBytes = imageBitmap.encodeToByteArray(imageType)
    val sharedFile = createSharedTempFile(fileName, imageType, imageBytes)

    copySharedFileToClipboard(sharedFile)

    if (tryOpenNativeShare(sharedFile)) {
        return
    }

    revealSharedFile(sharedFile)
}

private fun createSharedTempFile(
    fileName: String,
    imageType: CapturableSaveImageType,
    imageBytes: ByteArray
): File {
    val normalizedName = fileName
        .ifBlank { "capture_shared" }
        .replace(Regex("""[\\/:*?"<>|]"""), "_")
    val tempFile = Files.createTempFile(
        "capturable-",
        "-$normalizedName.${imageType.suffix}"
    ).toFile()

    tempFile.writeBytes(imageBytes)
    tempFile.deleteOnExit()
    return tempFile
}

private fun copySharedFileToClipboard(file: File) {
    runCatching {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(SharedFileTransferable(file), null)
    }
}

private fun tryOpenNativeShare(file: File): Boolean = when (desktopPlatform()) {
    DesktopPlatform.MACOS -> tryStart("open", "-a", "Mail", file.absolutePath)
    DesktopPlatform.LINUX -> tryStart("xdg-email", "--attach", file.absolutePath)
    DesktopPlatform.WINDOWS -> tryDesktopMail()
    DesktopPlatform.UNKNOWN -> false
}

private fun revealSharedFile(file: File) {
    when (desktopPlatform()) {
        DesktopPlatform.MACOS -> {
            if (tryStart("open", "-R", file.absolutePath)) return
        }

        DesktopPlatform.WINDOWS -> {
            if (tryStart("explorer.exe", "/select,${file.absolutePath}")) return
        }

        DesktopPlatform.LINUX -> {
            if (tryStart("xdg-open", file.parentFile.absolutePath)) return
        }

        DesktopPlatform.UNKNOWN -> Unit
    }

    runCatching {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file.parentFile)
            }
        }
    }
}

private fun tryDesktopMail(): Boolean {
    if (!Desktop.isDesktopSupported()) return false
    val desktop = Desktop.getDesktop()
    if (!desktop.isSupported(Desktop.Action.MAIL)) return false
    return runCatching {
        desktop.mail()
    }.isSuccess
}

private fun tryStart(vararg command: String): Boolean = runCatching {
    ProcessBuilder(*command)
        .redirectErrorStream(true)
        .start()
    true
}.getOrDefault(false)

private fun desktopPlatform(): DesktopPlatform {
    val osName = System.getProperty("os.name").orEmpty().lowercase(Locale.US)
    return when {
        osName.contains("mac") -> DesktopPlatform.MACOS
        osName.contains("win") -> DesktopPlatform.WINDOWS
        osName.contains("linux") -> DesktopPlatform.LINUX
        else -> DesktopPlatform.UNKNOWN
    }
}

private enum class DesktopPlatform {
    WINDOWS,
    MACOS,
    LINUX,
    UNKNOWN
}

private class SharedFileTransferable(private val file: File) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> =
        arrayOf(DataFlavor.javaFileListFlavor, DataFlavor.stringFlavor)

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean =
        flavor == DataFlavor.javaFileListFlavor || flavor == DataFlavor.stringFlavor

    override fun getTransferData(flavor: DataFlavor): Any = when (flavor) {
        DataFlavor.javaFileListFlavor -> listOf(file)
        DataFlavor.stringFlavor -> file.absolutePath
        else -> throw UnsupportedFlavorException(flavor)
    }
}