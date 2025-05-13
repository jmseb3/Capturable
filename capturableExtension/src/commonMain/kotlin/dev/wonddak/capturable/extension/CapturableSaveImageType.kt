package dev.wonddak.capturable.extension

import androidx.compose.ui.graphics.ImageBitmap

sealed class CapturableSaveImageType(
    open val quality: Int,
    val suffix: String
) {

    /**
     * share type PNG
     * @param quality compress quality(0 ~ 100)
     */
    data class PNG(override val quality: Int) : CapturableSaveImageType(quality, "png")

    /**
     * share type JPEG
     * @param quality compress quality(0 ~ 100)
     */
    data class JPEG(override val quality: Int) : CapturableSaveImageType(quality, "jpeg")

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
