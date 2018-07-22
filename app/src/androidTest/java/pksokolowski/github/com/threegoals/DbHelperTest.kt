package pksokolowski.github.com.threegoals

import android.support.test.InstrumentationRegistry
import org.junit.After

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import pksokolowski.github.com.threegoals.database.DbHelper
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.models.Goal
import pksokolowski.github.com.threegoals.models.Report
import java.util.*

class DbHelperInstrumentedTest {

    private lateinit var db: DbHelper
    @Before
    fun prepare() {
        InstrumentationRegistry.getTargetContext().deleteDatabase("dataBase.db")
        db = DbHelper.getInstance(InstrumentationRegistry.getTargetContext())
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getDays_isCorrect() {
        populateDbWithGenericDataSet1(db)

        val edition = EditionsManager.getCurrentEdition(context()) ?: throw Exception("Edition is null")
        val days = db.getDays(edition)

        var nullsCount = 0
        for(day in days){
            if(day == null) nullsCount ++
        }

        assertEquals("wrong number of null days", 6, nullsCount )

    }

    @Test
    fun isThereReportForDay_isCorrect() {

        val edition = Edition(1, "", 3, 0, 1)
        db.pushEdition(edition)
        db.pushGoal(Goal(-1, "goal1", "I", 0, 1))
        db.pushGoal(Goal(-1, "goal1", "II", 1, 1))
        db.pushGoal(Goal(-1, "goal1", "III", 2, 1))

        val wasReport = db.isThereReportForDay(0, edition)

        val reports = mutableListOf(
                Report(-1, 0, 0, 0, 0, 1),
                Report(-1, 0, 0, 0, 0, 2),
                Report(-1, 0, 0, 0, 0, 3)
        )
        db.pushReports(reports, edition)

        val isReport = db.isThereReportForDay(0, edition)

        assertNotEquals(wasReport, isReport)
    }

    private fun context() = InstrumentationRegistry.getTargetContext()

    /**
     * this is generating a data set which is supposed not to change between runs.
     * Do not edit, instead create a new set generator.
     */
    private fun populateDbWithGenericDataSet1(db: DbHelper) {
        val context = InstrumentationRegistry.getTargetContext()
        val rand = Random(345)

        EditionsManager.createEdition(context)
        val edition = EditionsManager.getCurrentEdition(context) ?: throw Exception("edition is null")
        val goals = db.getGoals(edition)

        val daysToSkip = arrayOf(0, 3, 13, 19, 200, 304)
        for (i in 0 until edition.length_in_days) {
            if(i in daysToSkip) continue

            val timestamp = edition.start_day_timestamp + TimeHelper.dayInMillis * i + rand.nextInt(5555)
            val reports = mutableListOf<Report>()

            for(ii in 0 until edition.goals_count){
                reports.add(Report(-1, i, timestamp, rand.nextInt(5), rand.nextInt(100),goals[ii].ID))
            }

            db.pushReports(reports, edition)
        }
    }
}