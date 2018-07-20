package pksokolowski.github.com.threegoals.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.models.Goal
import pksokolowski.github.com.threegoals.models.Report

class DbHelper private constructor(context: Context) : SQLiteOpenHelper(context, "dataBase.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        with(p0!!) {
            execSQL("CREATE TABLE " + Contract.reports.TABLE_NAME + " (" +
                    Contract.reports.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.reports.COLUMN_NAME_DAY_NUM + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_TIME_STAMP + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_SCORE_TRYING_HARD + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_SCORE_POSITIVES + " INTEGER, " +
                    Contract.reports.COLUMN_NAME_GOAL + " INTEGER," +
                    " FOREIGN KEY (" + Contract.reports.COLUMN_NAME_GOAL + ") REFERENCES " + Contract.goals.TABLE_NAME + "(" + Contract.goals.ID + "))")

            execSQL("CREATE TABLE " + Contract.goals.TABLE_NAME + " (" +
                    Contract.goals.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.goals.COLUMN_NAME_NAME + " TEXT, " +
                    Contract.goals.COLUMN_NAME_INITIAL + " TEXT, " +
                    Contract.goals.COLUMN_NAME_POSITION + " INTEGER, " +
                    Contract.goals.COLUMN_NAME_EDITION + " INTEGER, " +
                    " FOREIGN KEY (" + Contract.goals.COLUMN_NAME_EDITION + ") REFERENCES " + Contract.editions.TABLE_NAME + "(" + Contract.editions.ID + "))")

            execSQL("CREATE TABLE " + Contract.editions.TABLE_NAME + " (" +
                    Contract.editions.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Contract.editions.COLUMN_NAME_TITLE + " TEXT, " +
                    Contract.editions.COLUMN_NAME_GOALS_COUNT + " INTEGER, " +
                    Contract.editions.COLUMN_NAME_START_DAY_0_HOUR + " INTEGER, " +
                    Contract.editions.COLUMN_NAME_EDITION_LENGTH + " INTEGER)")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        with(p0!!) {
            execSQL("DROP TABLE IF EXISTS " + Contract.reports.TABLE_NAME)
            execSQL("DROP TABLE IF EXISTS " + Contract.goals.TABLE_NAME)
            execSQL("DROP TABLE IF EXISTS " + Contract.editions.TABLE_NAME)
            onCreate(this)
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: DbHelper? = null

        fun getInstance(context: Context): DbHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context): DbHelper {
            val helper = DbHelper(context)
            helper.sDataBase = helper.writableDatabase
            return helper
        }

    }

    private var sDataBase: SQLiteDatabase? = null

    // push methods:
    fun pushReport(report: Report): Long {
        val cv = ContentValues()
        cv.put(Contract.reports.COLUMN_NAME_DAY_NUM, report.day_num)
        cv.put(Contract.reports.COLUMN_NAME_TIME_STAMP, report.time_stamp)
        cv.put(Contract.reports.COLUMN_NAME_SCORE_TRYING_HARD, report.score_trying_hard)
        cv.put(Contract.reports.COLUMN_NAME_SCORE_POSITIVES, report.score_positives)
        cv.put(Contract.reports.COLUMN_NAME_GOAL, report.goal)
        return sDataBase!!.insert(Contract.reports.TABLE_NAME, null, cv)
    }

    fun pushEdition(edition: Edition): Long {
        val cv = ContentValues()
        cv.put(Contract.editions.COLUMN_NAME_TITLE, edition.title)
        cv.put(Contract.editions.COLUMN_NAME_GOALS_COUNT, edition.goals_count)
        cv.put(Contract.editions.COLUMN_NAME_START_DAY_0_HOUR, edition.start_day_timestamp)
        cv.put(Contract.editions.COLUMN_NAME_EDITION_LENGTH, edition.length_in_days)
        return sDataBase!!.insert(Contract.editions.TABLE_NAME, null, cv)
    }

    fun pushGoal(goal: Goal): Long {
        val cv = ContentValues()
        cv.put(Contract.goals.COLUMN_NAME_NAME, goal.name)
        cv.put(Contract.goals.COLUMN_NAME_INITIAL, goal.initial)
        cv.put(Contract.goals.COLUMN_NAME_POSITION, goal.position)
        cv.put(Contract.goals.COLUMN_NAME_EDITION, goal.edition)
        return sDataBase!!.insert(Contract.goals.TABLE_NAME, null, cv)
    }

    fun updateGoalCustomName(goal: Goal, newCustomName: String){
        val cv = ContentValues()
        cv.put(Contract.goals.COLUMN_NAME_NAME, newCustomName)
        val whereClause = Contract.goals.ID + " =? "
        val whereArgs = arrayOf(goal.ID.toString())
        sDataBase!!.update(Contract.goals.TABLE_NAME, cv, whereClause, whereArgs).toLong()
    }

    // get methods:
    fun getReports(edition: Edition): MutableList<Report> {

//        val projection = arrayOf(
//                Contract.reports.TABLE_NAME + "." + Contract.reports.ID,
//                Contract.reports.COLUMN_NAME_DAY_NUM,
//                Contract.reports.COLUMN_NAME_TIME_STAMP,
//                Contract.reports.COLUMN_NAME_SCORE_TRYING_HARD,
//                Contract.reports.COLUMN_NAME_SCORE_POSITIVES,
//                Contract.reports.COLUMN_NAME_GOAL
//        )
//
//        val fromClause = Contract.reports.TABLE_NAME + " JOIN " +
//                Contract.goals.TABLE_NAME + " ON " +
//                Contract.goals.TABLE_NAME+"."+Contract.goals.ID + " = " + Contract.reports.COLUMN_NAME_GOAL
//
//        val whereClause = Contract.goals.TABLE_NAME + "." + Contract.goals.COLUMN_NAME_EDITION + " = ? "
//        val whereArgs = arrayOf( edition.toString() )
//
//        val cursor = sDataBase!!.query(
//                fromClause,
//                projection,
//                whereClause,
//                whereArgs,
//                null, null,
//                Contract.reports.TABLE_NAME + ".rowid ASC"
//        )

        val cursor = sDataBase!!.rawQuery(
                "SELECT reports._id, day_num, time_stamp, score_trying_hard, score_positives, goal FROM reports JOIN goals ON goals._id = reports.goal WHERE edition = ? ORDER BY reports._id ASC",
                arrayOf(edition.ID.toString())
        )

        val reports = mutableListOf<Report>()

        while (cursor.moveToNext()) {
            reports.add(Report(
                    cursor.getLong(cursor.getColumnIndex(Contract.reports.ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_DAY_NUM)),
                    cursor.getLong(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_TIME_STAMP)),
                    cursor.getInt(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_SCORE_TRYING_HARD)),
                    cursor.getInt(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_SCORE_POSITIVES)),
                    cursor.getLong(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_GOAL)))
            )
        }

        cursor.close()
        return reports
    }

    fun getReportsForDay(edition: Edition, dayNumber: Int): MutableList<Report>{
        val cursor = sDataBase!!.rawQuery(
                "SELECT reports._id, day_num, time_stamp, score_trying_hard, score_positives, goal FROM reports JOIN goals ON goals._id = reports.goal WHERE edition = ? AND reports.day_num = ? ORDER BY reports._id ASC",
                arrayOf(edition.ID.toString(), dayNumber.toString())
        )

        val reports = mutableListOf<Report>()

        while (cursor.moveToNext()) {
            reports.add(Report(
                    cursor.getLong(cursor.getColumnIndex(Contract.reports.ID)),
                    cursor.getInt(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_DAY_NUM)),
                    cursor.getLong(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_TIME_STAMP)),
                    cursor.getInt(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_SCORE_TRYING_HARD)),
                    cursor.getInt(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_SCORE_POSITIVES)),
                    cursor.getLong(cursor.getColumnIndex(Contract.reports.COLUMN_NAME_GOAL)))
            )
        }

        cursor.close()
        return reports
    }

    fun getEditions(): MutableList<Edition> {
        val cursor = sDataBase!!.rawQuery("SELECT * FROM editions ORDER BY _id ASC", null)

        val editions = mutableListOf<Edition>()

        while (cursor.moveToNext()) {
            editions.add(Edition(
                    cursor.getLong(cursor.getColumnIndex(Contract.editions.ID)),
                    cursor.getString(cursor.getColumnIndex(Contract.editions.COLUMN_NAME_TITLE)),
                    cursor.getInt(cursor.getColumnIndex(Contract.editions.COLUMN_NAME_GOALS_COUNT)),
                    cursor.getLong(cursor.getColumnIndex(Contract.editions.COLUMN_NAME_START_DAY_0_HOUR)),
                    cursor.getInt(cursor.getColumnIndex(Contract.editions.COLUMN_NAME_EDITION_LENGTH)))
            )
        }

        cursor.close()
        return editions
    }

    fun getGoals(edition: Edition): MutableList<Goal> {
        val cursor = sDataBase!!.rawQuery("SELECT * FROM goals WHERE edition = ? ORDER BY position ASC", arrayOf(edition.ID.toString()))

        val goals = mutableListOf<Goal>()

        while (cursor.moveToNext()) {
            goals.add(Goal(
                    cursor.getLong(cursor.getColumnIndex(Contract.goals.ID)),
                    cursor.getString(cursor.getColumnIndex(Contract.goals.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(Contract.goals.COLUMN_NAME_INITIAL)),
                    cursor.getInt(cursor.getColumnIndex(Contract.goals.COLUMN_NAME_POSITION)),
                    cursor.getLong(cursor.getColumnIndex(Contract.goals.COLUMN_NAME_EDITION)))
            )
        }

        cursor.close()
        return goals
    }

    fun isThereReportForDay(dayNum: Int, edition: Edition) : Boolean{
        val cursor = sDataBase!!.rawQuery(
                "SELECT reports._id FROM reports JOIN goals ON goals._id = reports.goal WHERE goals.edition = ? AND reports.day_num = ?",
                arrayOf(edition.ID.toString(), dayNum.toString()))

        var count = cursor.count
        cursor.close()

        return count > 0
    }
}