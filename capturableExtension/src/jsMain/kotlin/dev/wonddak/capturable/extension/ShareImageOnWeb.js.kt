package dev.wonddak.capturable.extension

import kotlinx.browser.window
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.files.File

actual suspend fun shareImageOnWeb(bytes: ByteArray, fileName: String, mimeType: String) {
    // 1. ByteArray -> Uint8Array
    val uint8Array = Uint8Array(bytes.size)
    bytes.forEachIndexed { index, byte -> uint8Array[index] = byte }

    // 2. File 생성 (jsMain은 속성백을 dynamic으로 처리 가능)
    val file = File(
        arrayOf(uint8Array),
        fileName,
        js("{ type: mimeType }").unsafeCast<org.w3c.files.FilePropertyBag>()
    )

    // 3. Navigator Share 호출
    val navigator = window.navigator.asDynamic()
    val shareData = js("{}")
    shareData.title = fileName
    shareData.files = arrayOf(file)

    if (navigator.canShare != null && navigator.canShare(shareData) == true) {
        navigator.share(shareData).catch { e ->
            println("Share failed: $e")
        }
    } else {
        println("Web Share API not supported")
    }
}