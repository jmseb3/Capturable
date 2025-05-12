package dev.wonddak.capturable.extension

sealed class ImageSaveType(val suffix: String) {

    /**
     * share type PNG
     * @param quality compress quality(0 ~ 100)
     */
    data class PNG(val quality: Int) : ImageSaveType("png")

    /**
     * share type JPEG
     * @param quality compress quality(0 ~ 100)
     */
    data class JPEG(val quality: Int) : ImageSaveType("jpeg")

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