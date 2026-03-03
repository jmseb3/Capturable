package dev.wonddak.capturable.extension

import kotlin.js.Promise
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import org.w3c.files.File
import org.w3c.files.FilePropertyBag

// JS Object 구성을 위한 인터페이스
external interface ShareData : JsAny {
    var title: String?
    var files: JsArray<File>?
}

@JsName("Object")
external object JsObject {
    fun create(): ShareData
}

@JsName("navigator")
external object Navigator {
    fun canShare(data: ShareData): Boolean
    fun share(data: ShareData): Promise<JsAny?>
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun createShareData(): ShareData =
    js("({})")

@OptIn(ExperimentalWasmJsInterop::class)
actual suspend fun shareImageOnWeb(
    bytes: ByteArray,
    fileName: String,
    mimeType: String
) {
    val uint8Array = Uint8Array(bytes.size)
    bytes.forEachIndexed { index, byte ->
        uint8Array[index] = byte
    }

    val blobParts = JsArray<JsAny?>()
    blobParts[0] = uint8Array
    val blob = Blob(blobParts, BlobPropertyBag(type = mimeType))

    val fileParts = JsArray<JsAny?>()
    fileParts[0] = blob
    val file = File(fileParts, fileName, FilePropertyBag(type = mimeType))

    val shareData = createShareData()
    val filesArray = JsArray<File>()
    filesArray[0] = file

    shareData.title = fileName
    shareData.files = filesArray

    if (Navigator.canShare(shareData)) {
        Navigator.share(shareData)
    } else {
        println("Web Share API not supported")
    }
}