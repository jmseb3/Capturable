package dev.wonddak.capturableExample

import androidx.compose.runtime.Composable

@Composable
fun MobilApp() {
    App(
        otherContent = { scope, controller ->
            ShareButton(scope, controller)
        }
    )
}