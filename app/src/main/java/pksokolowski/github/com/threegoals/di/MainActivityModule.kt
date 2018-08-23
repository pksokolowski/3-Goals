package pksokolowski.github.com.threegoals.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pksokolowski.github.com.threegoals.MainActivity

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainActivity(): MainActivity
}