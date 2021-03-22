package com.yeongil.focusaid.data

data class BlockingApp(
    val rid: Int,
    val packageName: String,
    val timestamp: Long,
    val allowedTimeInSeconds: Int,
    val action: BlockingAppActionType,
) {
    enum class BlockingAppActionType { CLOSE, ALERT }
    companion object {
        const val ALL_APP = "ALL_APP"
    }
}