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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.Foundation.NSURL
import platform.Foundation.writeToURL
import platform.LinkPresentation.LPLinkMetadata
import platform.UIKit.UIActivityItemSourceProtocol
import platform.UIKit.UIActivityType
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIViewController
import platform.UIKit.popoverPresentationController
import platform.darwin.NSObject
import toNSData

/**
 * share Type of iOS
 */
sealed class ImageType(val suffix: String) {

    /**
     * share type PNG
     * @param quality compress quality(0 ~ 100)
     */
    data class PNG(val quality: Int) : ImageType("png")

    /**
     * share type JPEG
     * @param quality compress quality(0 ~ 100)
     */
    data class JPEG(val quality: Int) : ImageType("jpeg")
}

/**
 * Capture and share Image
 *
 * capture and saveTo [platform.Foundation.NSTemporaryDirectory] then make [UIActivityViewController] For share
 *
 * also see [CaptureController.captureAsync]
 *
 * Example usage:
 *
 * ```
 *  val captureController = rememberCaptureController()
 *  val uiScope = rememberCoroutineScope()
 *
 *  // The content to be captured in to Bitmap
 *  Column(
 *      modifier = Modifier.capturable(captureController),
 *  ) {
 *      // Composable content
 *  }
 *  Button(
 *     onClick = {
 *         scope.launch {
 *             captureController.captureAsyncAndShare()
 *         }
 *  }) { ... }
 * ```
 * @param [fileName]
 *
 * save temp File name
 * default value : "capture_shared"
 *
 * @param[metaTitle]
 *
 * title of UIActivityViewController meta
 * default value : "Share Captured Image"
 *
 * @param[shareType]
 *
 * Share Type PNG or JPEG [ImageType]
 *
 * @param[addOptionUIActivityViewController]
 *
 * add option if need for [UIActivityViewController]
 *
 * @param[topViewController]
 *
 * The topViewController where [UIActivityViewController] will be presented.
 * By default, it retrieves the top rootViewController.
 *
 */
@OptIn(ExperimentalForeignApi::class)
suspend fun CaptureController.captureAsyncAndShare(
    fileName: String = "capture_shared",
    metaTitle: String = "Share Captured Image",
    shareType: ImageType = ImageType.PNG(100),
    addOptionUIActivityViewController: (UIActivityViewController) -> Unit = {},
    topViewController: UIViewController? =
        UIApplication.sharedApplication.keyWindow?.rootViewController
) {
    val bitmap: ImageBitmap = this.captureAsync().await()
    val imageData: NSData = bitmap.toNSData(shareType) ?: return

    // Create a temporary file URL
    val tempDir = platform.Foundation.NSTemporaryDirectory()
    val tempUrl = NSURL.fileURLWithPath("$tempDir/$fileName." + shareType.suffix)

    // Write the PNG data to the temporary file
    imageData.writeToURL(tempUrl, true)

    val item = SingleImageProvider(tempUrl, metaTitle)

    val shareVC = UIActivityViewController(
        activityItems = listOf(item),
        applicationActivities = null
    )

    if (UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad) {
        // ipad need sourceView for show
        shareVC.popoverPresentationController?.sourceView = topViewController?.view
        val size = UIScreen.mainScreen.bounds.useContents { size }
        shareVC.popoverPresentationController?.sourceRect = CGRectMake(
            x = size.width / 2.1,
            y = size.height / 2.3,
            width = 200.0,
            height = 200.0
        )
    }
    addOptionUIActivityViewController(shareVC)

    topViewController?.presentViewController(
        viewControllerToPresent = shareVC,
        animated = true,
        completion = null
    )
}


internal class SingleImageProvider(private val imageUrl: NSURL, private val metaTitle: String) :
    NSObject(),
    UIActivityItemSourceProtocol {

    @ObjCSignatureOverride
    override fun activityViewController(
        activityViewController: UIActivityViewController,
        itemForActivityType: UIActivityType?
    ): Any? = imageUrl

    override fun activityViewControllerPlaceholderItem(
        activityViewController: UIActivityViewController
    ): Any = imageUrl

    @ExperimentalForeignApi
    override fun activityViewControllerLinkMetadata(
        activityViewController: UIActivityViewController
    ): objcnames.classes.LPLinkMetadata? {
        val metadata = LPLinkMetadata()
        metadata.title = metaTitle
        metadata.originalURL = imageUrl
        metadata.imageProvider = NSItemProvider(imageUrl)
        return metadata as objcnames.classes.LPLinkMetadata
    }
}