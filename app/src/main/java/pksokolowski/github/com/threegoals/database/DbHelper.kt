package pksokolowski.github.com.threegoals.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper private constructor (context: Context) : SQLiteOpenHelper(context, "dataBase.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        with(p0!!){
            execSQL("CREATE TABLE " + Contract.reports.TABLE_NAME + " (" +
                    Contract.reports.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.reports.COLUMN_NAME_DAY_NUM + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_TIME_STAMP + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_SCORE_TRYING_HARD + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_SCORE_POSITIVES + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_GOAL + " INTEGER," +
                    " FOREIGN KEY (" + Contract.reports.COLUMN_NAME_GOAL + ") REFERENCES " + Contract.goals.TABLE_NAME + "(" + Contract.goals.ID + "));")

            execSQL("CREATE TABLE " + Contract.goals.TABLE_NAME + " (" +
                    Contract.goals.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.goals.COLUMN_NAME_NAME + " TEXT, " +
                    Contract.goals.COLUMN_NAME_INITIAL+ " TEXT, " +
                    Contract.goals.COLUMN_NAME_POSITION + " INTEGER, " +
                    Contract.goals.COLUMN_NAME_EDITION + " INTEGER, " +
                    " FOREIGN KEY (" + Contract.goals.COLUMN_NAME_EDITION + ") REFERENCES " + Contract.editions.TABLE_NAME + "(" + Contract.editions.ID + "));")

            execSQL("CREATE TABLE " + Contract.editions.TABLE_NAME + " (" +
                    Contract.editions.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.editions.COLUMN_NAME_TITLE + " TEXT, " +
                    Contract.editions.COLUMN_NAME_GOALS_COUNT + " INTEGER, " +
                    Contract.editions.COLUMN_NAME_START_DAY_0_HOUR + " INTEGER, " +
                    Contract.editions.COLUMN_NAME_EDITION_LENGTH + " INTEGER);")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        with(p0!!){
            execSQL("DROP TABLE IF EXISTS "+ Contract.reports.TABLE_NAME)
            execSQL("DROP TABLE IF EXISTS "+ Contract.goals.TABLE_NAME)
            execSQL("DROP TABLE IF EXISTS "+ Contract.editions.TABLE_NAME)
            onCreate(this)
        }
    }
    companion object {

        @Volatile private var INSTANCE: DbHelper? = null

        fun getInstance(context: Context): DbHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) : DbHelper{
            var helper = DbHelper(context)
            helper.sDataBase = helper.writableDatabase
            return helper
        }

    }

    private var sDataBase: SQLiteDatabase? = null

}