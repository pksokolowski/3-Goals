package pksokolowski.github.com.threegoals.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pksokolowski.github.com.threegoals.MainActivityViewModel
import pksokolowski.github.com.threegoals.reporter.ReporterActivityViewModel
import pksokolowski.github.com.threegoals.viewmodel.ViewModelFactory

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReporterActivityViewModel::class)
    abstract fun bindReporterActivityViewModel(reporterActivityViewModel: ReporterActivityViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}