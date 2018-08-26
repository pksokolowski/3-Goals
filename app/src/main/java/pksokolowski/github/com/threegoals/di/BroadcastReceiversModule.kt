package pksokolowski.github.com.threegoals.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pksokolowski.github.com.threegoals.alarms.BootFinishedReceiver
import pksokolowski.github.com.threegoals.alarms.AlarmsReceiver
import pksokolowski.github.com.threegoals.notifications.NotificationsClickReceiver

@Suppress("unused")
@Module
abstract class BroadcastReceiversModule {
    @ContributesAndroidInjector()
    abstract fun contributeAlarmsReceiver(): AlarmsReceiver

    @ContributesAndroidInjector()
    abstract fun contributeBootFinishedReceiver(): BootFinishedReceiver

    @ContributesAndroidInjector()
    abstract fun contributeNotificationsClickReceiver(): NotificationsClickReceiver
}