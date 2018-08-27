package pksokolowski.github.com.threegoals.di

import android.app.Application
import android.arch.persistence.room.Room
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import pksokolowski.github.com.threegoals.data.*
import pksokolowski.github.com.threegoals.utils.DATABASE_NAME
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class, BroadcastReceiversModule::class, AndroidInjectionModule::class, AndroidSupportInjectionModule::class])
open class AppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room
                .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build()
    }

    @Singleton
    @Provides
    fun provideReportsDao(db: AppDatabase): ReportsDao {
        return db.daysDataDao()
    }

    @Singleton
    @Provides
    fun provideEditionsDao(db: AppDatabase): EditionsDao {
        return db.editionsDao()
    }

    @Singleton
    @Provides
    fun provideGoalsDao(db: AppDatabase): GoalsDao {
        return db.goalsDao()
    }
}