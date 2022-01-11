package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.BlockingApp
import com.yeongil.focusaid.dataSource.blockingAppDatabase.entity.BlockingAppEntity

fun BlockingApp.toEntity(): BlockingAppEntity {
    return BlockingAppEntity(
        rid,
        packageName,
        timestamp,
        allowedTimeInSeconds,
        allowedForThisExecution,
        action.toEntity()
    )
}

fun BlockingAppEntity.toData(): BlockingApp {
    return BlockingApp(
        rid,
        packageName,
        timestamp,
        allowedTimeInSeconds,
        allowedForThisExecution,
        action.toData()
    )
}

fun BlockingApp.BlockingAppActionType.toEntity(): BlockingAppEntity.BlockingAppActionTypeEntity {
    return BlockingAppEntity.BlockingAppActionTypeEntity.valueOf(name)
}

fun BlockingAppEntity.BlockingAppActionTypeEntity.toData(): BlockingApp.BlockingAppActionType {
    return BlockingApp.BlockingAppActionType.valueOf(name)
}
