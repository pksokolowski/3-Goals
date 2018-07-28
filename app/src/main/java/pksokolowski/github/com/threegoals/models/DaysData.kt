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

    fun getGoalInitialAt(pos: Int): String {
        if (pos !in goals.indices) return "?"
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

    fun getDailyScores(cumulative: Boolean = false, includeTryingHard: Boolean = true, includePositives: Boolean = true, goalNum: Int = -1): IntArray {
        val data = IntArray(edition.length_in_days)
        var cumulation = 0

        for (i in days.indices) {
            val day = days[i]
            if(day == null) {
                data[i] = cumulation
                continue
            }

            val score = ScoreCalculator.calc(day, includeTryingHard, includePositives, goalNum)
            if (cumulative) {
                cumulation += score
                data[i] = cumulation
            } else {
                data[i] = score
            }
        }
        return data
    }

    fun getDaysOfWeekAverageScores(includeTryingHard: Boolean = true, includePositives: Boolean = true, goalNum: Int): IntArray{
        val daysOfWeek = IntArray(7)
        val weights = IntArray(7)
        for (i in days.indices){
            val day = days[i] ?: continue
            val score = ScoreCalculator.calc(day, includeTryingHard, includePositives, goalNum)
            val dayOfWeek = edition.dayOfWeekByDayNum(i) -1
            daysOfWeek[dayOfWeek] += score
            weights[dayOfWeek] +=1
        }
        // calculate averages
        val results = IntArray(7)
        for(i in daysOfWeek.indices){
            if(weights[i] == 0) continue
            results[i] = (daysOfWeek[i] / weights[i].toFloat()).toInt()
        }
        return results
    }

}