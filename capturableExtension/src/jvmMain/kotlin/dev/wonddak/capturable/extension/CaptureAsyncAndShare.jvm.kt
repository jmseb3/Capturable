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
import java.awt.Image
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

@ExperimentalCapturableShareApi
actual suspend fun CaptureController.captureAsyncAndShare(
    fileName: String,
    imageType: CapturableSaveImageType
) {
    val imageBitmap = this.captureAsync().await()
    val clipboardImageType = imageType.forClipboard()
    val imageBytes = imageBitmap.encodeToByteArray(clipboardImageType)
    val clipboardImage = imageBytes.decodeClipboardImage()
    copySharedImageToClipboard(clipboardImage)
}

private fun CapturableSaveImageType.forClipboard(): CapturableSaveImageType = when (this) {
    is CapturableSaveImageType.WEBP -> CapturableSaveImageType.PNG(100)
    else -> this
}

private fun ByteArray.decodeClipboardImage(): Image =
    ByteArrayInputStream(this).use(ImageIO::read)
        ?: error("Failed to decode the captured image for the system clipboard.")

private fun copySharedImageToClipboard(image: Image) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(SharedImageTransferable(image), null)
}

private class SharedImageTransferable(private val image: Image) : Transferable {
    override fun getTransferDataFlavors(): Array<DataFlavor> =
        arrayOf(DataFlavor.imageFlavor)

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean =
        flavor == DataFlavor.imageFlavor

    override fun getTransferData(flavor: DataFlavor): Any = when (flavor) {
        DataFlavor.imageFlavor -> image
        else -> throw UnsupportedFlavorException(flavor)
    }
}
