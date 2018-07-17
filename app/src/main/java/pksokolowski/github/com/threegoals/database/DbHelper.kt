package pksokolowski.github.com.threegoals.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper private constructor (context: Context) : SQLiteOpenHelper(context, "dataBase.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS "+ Contract.reports.TABLE_NAME)
        p0.execSQL("DROP TABLE IF EXISTS "+ Contract.goals.TABLE_NAME)
        p0.execSQL("DROP TABLE IF EXISTS "+ Contract.editions.TABLE_NAME)
        onCreate(p0)
    }
    companion object {

        @Volatile private var INSTANCE: DbHelper? = null

        fun getInstance(context: Context): DbHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) = DbHelper(context)

    }

}