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
package dev.wonddak.capturable.extension.platform.macos

import androidx.compose.ui.graphics.ImageBitmap
import dev.wonddak.capturable.extension.CapturableSaveImageType
import dev.wonddak.capturable.extension.encodeToByteArray
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.PosixFilePermission
import kotlin.io.path.absolutePathString
import kotlin.io.path.setPosixFilePermissions

suspend fun shareByMacShare(
    imageBitmap: ImageBitmap,
    fileName: String,
    imageType: CapturableSaveImageType
) {
    val shareImageType = imageType.forMacShare()
    val imageBytes = imageBitmap.encodeToByteArray(shareImageType)

    val imageFile = createTempImageFile(
        fileName = fileName,
        imageType = shareImageType,
        bytes = imageBytes
    )

    val macShareExecutable = extractExecutableResource(
        resourcePath = "native/macos/mac-share"
    )

    ProcessBuilder(
        macShareExecutable.absolutePathString(),
        "--file",
        imageFile.absolutePathString()
    )
        .redirectErrorStream(true)
        .start()
}

private fun CapturableSaveImageType.forMacShare(): CapturableSaveImageType = when (this) {
    is CapturableSaveImageType.WEBP -> CapturableSaveImageType.PNG(100)
    else -> this
}

private fun CapturableSaveImageType.fileExtension(): String = when (this) {
    is CapturableSaveImageType.PNG -> "png"
    is CapturableSaveImageType.JPEG -> "jpg"
    is CapturableSaveImageType.WEBP -> "webp"
}

private fun createTempImageFile(
    fileName: String,
    imageType: CapturableSaveImageType,
    bytes: ByteArray
): Path {
    val extension = imageType.fileExtension()

    val sanitizedName = fileName
        .substringBeforeLast(".")
        .ifBlank { "capturable-share" }

    val tempFile = Files.createTempFile(
        sanitizedName,
        ".$extension"
    )

    Files.write(tempFile, bytes)
    tempFile.toFile().deleteOnExit()

    return tempFile
}

private fun extractExecutableResource(resourcePath: String): Path {
    val inputStream = Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream(resourcePath)
        ?: error("Resource not found: $resourcePath")

    val executable = Files.createTempFile("mac-share-", "")

    inputStream.use { input ->
        Files.copy(input, executable, StandardCopyOption.REPLACE_EXISTING)
    }

    executable.toFile().deleteOnExit()
    setExecutablePermission(executable)

    return executable
}

private fun setExecutablePermission(path: Path) {
    runCatching {
        path.setPosixFilePermissions(
            setOf(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_EXECUTE,
                PosixFilePermission.GROUP_READ,
                PosixFilePermission.GROUP_EXECUTE,
                PosixFilePermission.OTHERS_READ,
                PosixFilePermission.OTHERS_EXECUTE
            )
        )
    }.onFailure {
        path.toFile().setExecutable(true)
    }
}