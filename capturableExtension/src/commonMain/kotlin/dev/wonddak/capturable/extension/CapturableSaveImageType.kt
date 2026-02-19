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

/**
 * Defines the image format for saving or sharing a captured composable.
 * This sealed class provides options for PNG, JPEG, and WEBP formats, each with a configurable quality setting.
 *
 * @param quality The compression quality of the image, ranging from 0 (lowest) to 100 (highest).
 * @param suffix The file extension for the image type (e.g., "png", "jpeg").
 */
sealed class CapturableSaveImageType(open val quality: Int, val suffix: String) {

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
     * share type WEBP
     * @param quality compress quality(0 ~ 100)
     */
    data class WEBP(override val quality: Int) : CapturableSaveImageType(quality, "webp")

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