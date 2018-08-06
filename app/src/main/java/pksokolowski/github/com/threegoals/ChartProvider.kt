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
            // the number of days of edition to display
            val dayOfEditionToday = data.edition.dayNumOf(TimeHelper.now())
            val daysOfEdition = if (dayOfEditionToday != -1) dayOfEditionToday else data.edition.length_in_days

            val firstDimMaxValue = ScoreCalculator.getMaxDailyScore(data.edition, true, false, goalPosition != -1)
            val secondDimMaxValue = ScoreCalculator.getMaxDailyScore(data.edition, false, true, goalPosition != -1)

            when (type) {
                TYPE_DELTA -> {
                    val firstDimScores = data.getDailyScores(includePositives = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val secondDimScores = data.getDailyScores(includeTryingHard = false, goalNum = goalPosition, dayCap = daysOfEdition)

                    return BarChart(context, firstDimScores, firstDimMaxValue, secondDimScores, secondDimMaxValue)
                }
                TYPE_SUM -> {
                    val firstDimScores = data.getDailyScores(cumulative = true, includePositives = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val secondDimScores = data.getDailyScores(cumulative = true, includeTryingHard = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val firstDimMaxVal = if(daysOfEdition == 0) 1 else firstDimMaxValue * daysOfEdition
                    val secondDimMaxVal = if(daysOfEdition == 0) 1 else secondDimMaxValue * daysOfEdition

                    return BarChart(context, firstDimScores, firstDimMaxVal, secondDimScores, secondDimMaxVal)
                }
                TYPE_WEEK -> {
                    val firstDimScores = data.getDaysOfWeekAverageScores(includePositives = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val secondDimScores = data.getDaysOfWeekAverageScores(includeTryingHard = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    // prepare days of week names
                    val dayNames = Array(7) { TimeHelper.getDayOfWeekShortNameByDaysNumber(it + 1) }

                    return BarChart(context, firstDimScores, firstDimMaxValue, secondDimScores, secondDimMaxValue, dayNames)
                }
            }
            return View(context)
        }
    }
}