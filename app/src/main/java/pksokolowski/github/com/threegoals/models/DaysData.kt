package pksokolowski.github.com.threegoals.models

import android.content.Context
import pksokolowski.github.com.threegoals.ScoreCalculator
import pksokolowski.github.com.threegoals.database.DbHelper

class DaysData(context: Context, val edition: Edition) {
    private val goals: MutableList<Goal>
    private val days: Array<Day?>
    val nonNullCount: Int

    init {
        val db = DbHelper.getInstance(context)
        goals = db.getGoals(edition)
        days = db.getDays(edition)

        var daysCounter = 0
        for (day in days) {
            if (day != null) daysCounter++
        }
        nonNullCount = daysCounter
    }

    fun isReportPresentForDay(dayNum: Int): Boolean {
        if (dayNum !in days.indices) return false
        return days[dayNum] != null
    }

    fun getGoalInitialAt(pos: Int): String{
        if(pos !in goals.indices) return "?"
        return goals[pos].initial
    }

    fun getScoresPerGoal(): IntArray {
        val scores = IntArray(edition.goals_count)
        for (day in days) {
            if (day != null) {
                for (i: Int in 0 until edition.goals_count) {
                    scores[i] += ScoreCalculator.calc(day.reports[i])
                }
            }
        }
        return scores
    }
}