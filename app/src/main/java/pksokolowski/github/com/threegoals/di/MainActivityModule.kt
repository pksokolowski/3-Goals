package pksokolowski.github.com.threegoals.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pksokolowski.github.com.threegoals.MainActivity
import pksokolowski.github.com.threegoals.TopBarFragment
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeEditorDialogFragment(): EditorDialogFragment

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun contributeTopBarFragment(): TopBarFragment
}