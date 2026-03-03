package dev.wonddak.capturable.extension.annotation

@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING, // 또는 ERROR
    message = "This API is experimental and may change."
)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY
)
annotation class ExperimentalCaptureApi