package pksokolowski.github.com.threegoals.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.model.Goal
import pksokolowski.github.com.threegoals.model.Report

@Database(
        entities = [
            Report::class,
            Goal::class,
            Edition::class],
        version = 2,
        exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun daysDataDao(): ReportsDao
    abstract fun editionsDao(): EditionsDao
    abstract fun goalsDao(): GoalsDao

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // create a replacement tables with new schema for 'reports' and 'goals'
                database.execSQL("""
                   CREATE TABLE IF NOT EXISTS `reports_new` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `dayNum` INTEGER NOT NULL,
                    `timeStamp` INTEGER NOT NULL,
                    `scoreTryingHard` INTEGER NOT NULL,
                    `scorePositives` INTEGER NOT NULL,
                    `goal` INTEGER NOT NULL,
                    FOREIGN KEY(`goal`) REFERENCES `goals`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )
                """.trimIndent())
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `goals_new` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `name` TEXT NOT NULL,
                    `initial` TEXT NOT NULL,
                    `position` INTEGER NOT NULL,
                    `edition` INTEGER NOT NULL,
                    FOREIGN KEY(`edition`) REFERENCES `editions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )
                """.trimIndent())

                // insert data from the old tables to replacement ones
                database.execSQL("""
                    INSERT INTO reports_new (id, dayNum, timeStamp, scoreTryingHard, scorePositives, goal)
                    SELECT id, dayNum, timeStamp, scoreTryingHard, scorePositives, goal FROM reports
                """.trimIndent())
                database.execSQL("""
                    INSERT INTO goals_new (id, name, initial, position, edition)
                    SELECT id, name, initial, position, edition FROM goals
                """.trimIndent())

                // get rid of the old tables
                database.execSQL("DROP TABLE reports")
                database.execSQL("DROP TABLE goals")

                // rename the replacement tables to names of the original tables to replace them
                database.execSQL("ALTER TABLE reports_new RENAME TO reports")
                database.execSQL("ALTER TABLE goals_new RENAME TO goals")

                // add indexes to the tables to complement the foreign keys added in this migration
                // this will improve performance in some cases, as full table scans won't be
                // necessary anymore for those foreign keys.
                database.execSQL("CREATE INDEX `index_reports_goal` ON `reports` (`goal`)")
                database.execSQL("CREATE INDEX `index_goals_edition` ON `goals` (`edition`)")
            }
        }
    }
}