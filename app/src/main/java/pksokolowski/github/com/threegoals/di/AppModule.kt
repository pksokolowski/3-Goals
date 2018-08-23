package pksokolowski.github.com.threegoals.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import pksokolowski.github.com.threegoals.data.AppDatabase
import pksokolowski.github.com.threegoals.data.DaysDataDao
import pksokolowski.github.com.threegoals.data.EditionsDao
import pksokolowski.github.com.threegoals.utils.DATABASE_NAME
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, AndroidInjectionModule::class])
open class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room
                .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

    @Singleton
    @Provides
    fun provideDaysDataDao(db: AppDatabase): DaysDataDao {
        return db.daysDataDao()
    }

    @Singleton
    @Provides
    fun provideEditionsDao(db: AppDatabase): EditionsDao {
        return db.editionsDao()
    }
}