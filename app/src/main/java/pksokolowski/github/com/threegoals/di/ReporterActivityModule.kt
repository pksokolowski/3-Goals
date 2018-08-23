package pksokolowski.github.com.threegoals.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pksokolowski.github.com.threegoals.reporter.ReporterActivity

@Suppress("unused")
@Module
abstract class ReporterActivityModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeReporterActivity(): ReporterActivity
}