package dev.wonddak.capturable

import kotlinx.browser.document
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.File

actual fun download(
    byteArray: ByteArray,
    fileName: String,
) {
    // Create a blob
    val file = File(
        fileBits = byteArray.toBitsArray(),
        fileName = fileName,
    )

    // Create a element
    val a = document.createElement("a") as HTMLAnchorElement
    a.href = URL.createObjectURL(file)
    a.download = fileName

    // Trigger the download
    a.click()
}

fun ByteArray.toBitsArray(): Array<Uint8Array> {
    return arrayOf(Uint8Array(this.toTypedArray()))
}