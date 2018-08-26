package pksokolowski.github.com.threegoals.charts

import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.utils.ColorHelper
import pksokolowski.github.com.threegoals.utils.TimeHelper

/**
 * Converts DaysData into a list of PieChart's own Datum subclass objects which the PieChart can
 * understand and display.
 *
 * It also decides which scores from the data are going to be displayed on which dimension of the
 * PieChart (available dimensions include: slice size, slice color).
 *
 * The way of ensuring that scores are handled coherently throughout the app is using app-wide
 * ColorHelper for the color dimension, and using other scores as provided by DaysData getters.
 */
class PieChartDataConverter {
    companion object {
        fun convert(data: DaysData): MutableList<PieChart.Datum> {
            // obtain amount of days passed, used to calculate the max possible scores
            val daysOfEditionPassed = data.edition.daysOfEditionPassed(TimeHelper.now())

            // specific scores are extracted from data provided.
            val tryingHardPerGoal = data.getScoresPerGoal(includePositives = false, dayCap = daysOfEditionPassed)
            val positivesPerGoal = data.getScoresPerGoal(includeTryingHard = false, dayCap = daysOfEditionPassed)

            // score "positives" is expressed via color, using app-wide ColorHelper to keep it coherent
            val sliceColors = ColorHelper.getScoreInColor(positivesPerGoal.values, positivesPerGoal.maxValue)

            // a list of results is created and filled
            val pieData = mutableListOf<PieChart.Datum>()
            for (i in 0 until data.edition.goalsCount) {
                pieData.add(PieChart.Datum(data.getGoalInitialAt(i), tryingHardPerGoal.values[i].toLong(), i.toLong(), sliceColors[i]))
            }

            return pieData
        }
    }
}