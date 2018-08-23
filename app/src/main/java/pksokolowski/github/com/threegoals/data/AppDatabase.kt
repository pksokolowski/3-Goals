package pksokolowski.github.com.threegoals.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Goal
import pksokolowski.github.com.threegoals.model.Report

@Database(
        entities = [
            Report::class,
            Goal::class,
            Edition::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun daysDataDao(): DaysDataDao
    abstract fun editionsDao(): EditionsDao
}