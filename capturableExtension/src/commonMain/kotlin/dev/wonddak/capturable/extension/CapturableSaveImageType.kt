package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap

sealed class CapturableSaveImageType(val suffix: String) {

    /**
     * share type PNG
     * @param quality compress quality(0 ~ 100)
     */
    data class PNG(val quality: Int) : CapturableSaveImageType("png")

    /**
     * share type JPEG
     * @param quality compress quality(0 ~ 100)
     */
    data class JPEG(val quality: Int) : CapturableSaveImageType("jpeg")

    /**
     * File mimeType
     */
    internal val mimeType: String
        get() = "image/$suffix"

    /**
     * @return make file name with suffix
     */
    internal fun makeFileName(name: String): String = "$name.$suffix"
}

/**
 * ImageBitmap to ByteArray bt type
 * @param[type] image Type
 */
expect fun ImageBitmap.toByteArray(type: CapturableSaveImageType): ByteArray
