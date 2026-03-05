package dev.wonddak.capturable.extension

import dev.wonddak.capturable.controller.CaptureController
import io.github.vinceglb.filekit.utils.Platform
import io.github.vinceglb.filekit.utils.PlatformUtil
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

actual suspend fun CaptureController.captureAsyncAndShare(
    fileName: String,
    imageType: CapturableSaveImageType
) {
    val imageBitmap = this.captureAsync().await()
    val imageBytes = imageBitmap.encodeToByteArray(imageType)
    shareImage(imageBytes)
}

fun shareImage(bytes: ByteArray) {

    when(PlatformUtil.current) {
        Platform.Linux -> copyClipboard(bytes)
        Platform.MacOS -> copyClipboard(bytes)
        Platform.Windows -> copyClipboard(bytes)
    }
}

fun copyClipboard(bytes: ByteArray) {

    val image: BufferedImage = ImageIO.read(ByteArrayInputStream(bytes))

    val transferable = object : Transferable {

        override fun getTransferDataFlavors() =
            arrayOf(DataFlavor.imageFlavor)

        override fun isDataFlavorSupported(flavor: DataFlavor) =
            flavor == DataFlavor.imageFlavor

        override fun getTransferData(flavor: DataFlavor): Any {
            if (flavor == DataFlavor.imageFlavor) return image
            throw UnsupportedOperationException()
        }
    }

    Toolkit.getDefaultToolkit()
        .systemClipboard
        .setContents(transferable, null)
}