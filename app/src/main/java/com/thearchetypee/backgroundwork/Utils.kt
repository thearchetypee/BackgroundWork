package com.thearchetypee.backgroundwork

enum class Buttons {
    IntentService, Service, BoundService, ForegroundService, WorkManager
}

fun getButtons(): List<Buttons> {
    return listOf(Buttons.IntentService, Buttons.Service, Buttons.BoundService, Buttons.ForegroundService, Buttons.WorkManager)
}

enum class BoundServiceButtons {
    SayHello, GetRandomNumber, UnbindService, BindService
}

fun getBoundServiceButtons(): List<BoundServiceButtons> {
    return listOf(BoundServiceButtons.SayHello, BoundServiceButtons.GetRandomNumber, BoundServiceButtons.UnbindService, BoundServiceButtons.BindService)
}

enum class ForegroundServiceButtons {
    StartForegroundService, StopForegroundService, BindService, UnbindService, SayHello
}

fun getForegroundServiceButtons(): List<ForegroundServiceButtons> {
    return listOf(
        ForegroundServiceButtons.StartForegroundService,
        ForegroundServiceButtons.StopForegroundService,
        ForegroundServiceButtons.BindService,
        ForegroundServiceButtons.UnbindService,
        ForegroundServiceButtons.SayHello
    )
}

enum class WorkManagerButtons {
    StartWorkManager, ScheduleWorkManager, StopWorkManager
}

fun getWorkManagerButtons(): List<WorkManagerButtons> {
    return listOf(
        WorkManagerButtons.StartWorkManager,
        WorkManagerButtons.ScheduleWorkManager,
        WorkManagerButtons.StopWorkManager
    )
}
