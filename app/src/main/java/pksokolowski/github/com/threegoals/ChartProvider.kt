package pksokolowski.github.com.threegoals

import android.content.Context
import android.view.View
import pksokolowski.github.com.threegoals.charts.BarChart
import pksokolowski.github.com.threegoals.models.DaysData

class ChartProvider {
    companion object {
        const val TYPE_DELTA = 0
        const val TYPE_SUM = 1
        const val TYPE_WEEK = 2

        fun getChart(context: Context, data: DaysData, type: Int, goalPosition: Int): View {
            // amount of goals for which data is requested to be on the chart
            // -1 means no selection, so all goals are taken in, otherwise just one
            val goalsInvolved = if (goalPosition == -1) data.edition.goals_count else 1

            val firstDimensionMaxValue = ScoreCalculator.getMaxDailyScore(data.edition, true, false, goalPosition != -1)
            val secondDimensionMaxValue = ScoreCalculator.getMaxDailyScore(data.edition, false, true, goalPosition != -1)

            when (type) {
                TYPE_DELTA -> {
                    val firstDimensionScores = data.getDailyScores(includePositives = false, goalNum = goalPosition)
                    val secondDimensionScores = data.getDailyScores(includeTryingHard = false, goalNum = goalPosition)
                    return BarChart(context, firstDimensionScores, firstDimensionMaxValue, secondDimensionScores, secondDimensionMaxValue)
                }
                TYPE_SUM -> {
                    val firstDimensionScores = data.getDailyScores(cumulative = true, includePositives = false, goalNum = goalPosition)
                    val secondDimensionScores = data.getDailyScores(cumulative = true, includeTryingHard = false, goalNum = goalPosition)
                    return BarChart(context, firstDimensionScores, firstDimensionMaxValue * data.edition.length_in_days, secondDimensionScores, secondDimensionMaxValue* data.edition.length_in_days)
                }
                TYPE_WEEK -> {
                    val firstDimensionScores = data.getDaysOfWeekAverageScores(includePositives = false, goalNum = goalPosition)
                    val secondDimensionScores = data.getDaysOfWeekAverageScores(includeTryingHard = false, goalNum = goalPosition)
                    // prepare days of week names
                    val dayNames = Array(7){ TimeHelper.getDayOfWeekShortNameByDaysNumber(it+1)}
                    return BarChart(context, firstDimensionScores, firstDimensionMaxValue, secondDimensionScores, secondDimensionMaxValue, dayNames)
                }
            }
            return View(context)
        }
    }
}