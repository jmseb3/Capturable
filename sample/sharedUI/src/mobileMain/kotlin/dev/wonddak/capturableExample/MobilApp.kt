package dev.wonddak.capturableExample

import androidx.compose.runtime.Composable

@Composable
fun MobilApp() {
    App(
        otherContent = { imageSaveState, scope, controller ->
            ShareButton(imageSaveState, scope, controller)
        }
    )
}