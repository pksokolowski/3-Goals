package pksokolowski.github.com.threegoals.model

import android.content.Context
import pksokolowski.github.com.threegoals.ScoreCalculator
import pksokolowski.github.com.threegoals.data.DbHelper

class DaysData(context: Context, val edition: Edition) {
    private val goals: MutableList<Goal>
    private val days: Array<Day?>

    init {
        val db = DbHelper.getInstance(context)
        goals = db.getGoals(edition)
        days = db.getDays(edition)
    }

    fun isReportPresentForDay(dayNum: Int): Boolean {
        if (dayNum !in days.indices) return false
        return days[dayNum] != null
    }

    fun getGoalInitialAt(pos: Int): String {
        if (pos !in goals.indices) return "?"
        return goals[pos].initial
    }

    class Scores(val values: IntArray, val maxValue: Int)

    fun getScoresPerGoal(includeTryingHard: Boolean = true, includePositives: Boolean = true, dayCap: Int = edition.lengthInDays): Scores {
        val scores = IntArray(edition.goalsCount)
        for (d in 0 until dayCap) {
            val day = days[d]
            if (day != null) {
                for (i: Int in 0 until edition.goalsCount) {
                    scores[i] += ScoreCalculator.calc(day.reports[i], includeTryingHard, includePositives)
                }
            }
        }
        val maxValue = dayCap * ScoreCalculator.getMaxDailyScore(edition, includeTryingHard, includePositives, true)
        return Scores(scores, maxValue)
    }

    fun getDailyScores(cumulative: Boolean = false, includeTryingHard: Boolean = true, includePositives: Boolean = true, goalNum: Int = -1, dayCap: Int = edition.lengthInDays): Scores {
        val data = IntArray(dayCap)
        var cumulation = 0

        for (i in 0 until dayCap) {
            val day = days[i]
            if (day == null) {
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
        val multiplier = if(cumulative) dayCap else 1
        val maxValue = multiplier * ScoreCalculator.getMaxDailyScore(edition, includeTryingHard, includePositives, goalNum != -1)
        return Scores(data, maxValue)
    }

    fun getDaysOfWeekAverageScores(includeTryingHard: Boolean = true, includePositives: Boolean = true, goalNum: Int, dayCap: Int = edition.lengthInDays): Scores {
        val daysOfWeek = IntArray(7)
        val weights = IntArray(7)
        for (i in 0 until dayCap) {
            // increase weight for the day as it had zero score which needs to be counted
            val dayOfWeek = edition.dayOfWeekByDayNum(i) - 1
            if (days[i] == null) {
                weights[dayOfWeek] += 1
            }

            val day = days[i] ?: continue
            val score = ScoreCalculator.calc(day, includeTryingHard, includePositives, goalNum)
            daysOfWeek[dayOfWeek] += score
            weights[dayOfWeek] += 1
        }
        // calculate averages
        val results = IntArray(7)
        for (i in daysOfWeek.indices) {
            if (weights[i] == 0) continue
            results[i] = (daysOfWeek[i] / weights[i].toFloat()).toInt()
        }
        val maxValue = ScoreCalculator.getMaxDailyScore(edition, includeTryingHard, includePositives, goalNum != -1)
        return Scores(results, maxValue)
    }

}