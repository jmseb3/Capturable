package dev.wonddak.capturableExample

import kotlinx.browser.window

actual val maxFrame: Float
    get() {
        val isMobile = Regex("android|iphone|ipad|mobile", RegexOption.IGNORE_CASE)
            .containsMatchIn(window.navigator.userAgent)

        return if (isMobile) 1.0f else 0.2f
    }