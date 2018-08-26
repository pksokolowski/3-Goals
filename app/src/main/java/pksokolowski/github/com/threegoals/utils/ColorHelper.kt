package pksokolowski.github.com.threegoals.utils

import android.graphics.Color

class ColorHelper{
    companion object {
        fun getScoreInColor(value: Int, maxValue: Int): Int
        {
            val baseColor = Color.GRAY
            val r = Color.red(baseColor)
            val g = Color.green(baseColor)
            val b = Color.blue(baseColor)

            // modify green:
            val rangeLeft = 255 - g
            val percentageOfMaxScore = value / maxValue.toFloat()

            val shiftedG = Math.min(255F, g + (rangeLeft * percentageOfMaxScore))
            return Color.argb(255, r, shiftedG.toInt(), b)

        }

        fun getScoreInColor(values: IntArray, maxValue: Int): IntArray{
            val results = IntArray(values.size)
            values.forEachIndexed {i, value -> results[i] = getScoreInColor(value, maxValue) }
            return results
        }
    }

}