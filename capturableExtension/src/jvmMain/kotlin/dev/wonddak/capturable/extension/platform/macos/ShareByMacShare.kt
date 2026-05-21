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
    imageType: CapturableSaveImageType,
) {
    val shareImageType = imageType.forMacShare()
    val imageBytes = imageBitmap.encodeToByteArray(shareImageType)

    val imageFile = createTempImageFile(
        fileName = fileName,
        imageType = shareImageType,
        bytes = imageBytes,
    )

    val macShareExecutable = extractExecutableResource(
        resourcePath = "native/macos/mac-share",
    )

    ProcessBuilder(
        macShareExecutable.absolutePathString(),
        "--file",
        imageFile.absolutePathString(),
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
    bytes: ByteArray,
): Path {
    val extension = imageType.fileExtension()

    val sanitizedName = fileName
        .substringBeforeLast(".")
        .ifBlank { "capturable-share" }

    val tempFile = Files.createTempFile(
        sanitizedName,
        ".$extension",
    )

    Files.write(tempFile, bytes)
    tempFile.toFile().deleteOnExit()

    return tempFile
}

private fun extractExecutableResource(
    resourcePath: String,
): Path {
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
                PosixFilePermission.OTHERS_EXECUTE,
            )
        )
    }.onFailure {
        path.toFile().setExecutable(true)
    }
}