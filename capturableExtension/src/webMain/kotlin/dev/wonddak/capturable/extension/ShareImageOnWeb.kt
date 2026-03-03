package dev.wonddak.capturable.extension

// webMain
expect suspend fun shareImageOnWeb(
    bytes: ByteArray,
    fileName: String,
    mimeType: String
)