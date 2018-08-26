package pksokolowski.github.com.threegoals.charts

import android.content.Context
import android.view.View
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.utils.TimeHelper

class ChartProvider {
    companion object {
        const val TYPE_DELTA = 0
        const val TYPE_SUM = 1
        const val TYPE_WEEK = 2

        fun getChart(context: Context, data: DaysData, type: Int, goalPosition: Int): View {
            // the number of days of edition to display
            val daysOfEdition = data.edition.daysOfEditionPassed(TimeHelper.now())

            when (type) {
                TYPE_DELTA -> {
                    val firstDimScores = data.getDailyScores(includePositives = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val secondDimScores = data.getDailyScores(includeTryingHard = false, goalNum = goalPosition, dayCap = daysOfEdition)

                    return BarChart(context, firstDimScores.values, firstDimScores.maxValue, secondDimScores.values, secondDimScores.maxValue)
                }
                TYPE_SUM -> {
                    val firstDimScores = data.getDailyScores(cumulative = true, includePositives = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val secondDimScores = data.getDailyScores(cumulative = true, includeTryingHard = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val firstDimMaxVal = if(daysOfEdition == 0) 1 else firstDimScores.maxValue
                    val secondDimMaxVal = if(daysOfEdition == 0) 1 else secondDimScores.maxValue

                    return BarChart(context, firstDimScores.values, firstDimMaxVal, secondDimScores.values, secondDimMaxVal)
                }
                TYPE_WEEK -> {
                    val firstDimScores = data.getDaysOfWeekAverageScores(includePositives = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    val secondDimScores = data.getDaysOfWeekAverageScores(includeTryingHard = false, goalNum = goalPosition, dayCap = daysOfEdition)
                    // prepare days of week names
                    val dayNames = Array(7) { TimeHelper.getDayOfWeekShortNameByDaysNumber(it + 1) }

                    return BarChart(context, firstDimScores.values, firstDimScores.maxValue, secondDimScores.values, secondDimScores.maxValue, dayNames)
                }
            }
            return View(context)
        }
    }
}