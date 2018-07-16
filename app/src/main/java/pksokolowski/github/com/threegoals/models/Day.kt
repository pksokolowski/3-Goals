package pksokolowski.github.com.threegoals.models

import java.util.*

data class Day(val num: Int, val reports: Array<Report>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Day

        if (num != other.num) return false
        if (!Arrays.equals(reports, other.reports)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = num
        result = 31 * result + Arrays.hashCode(reports)
        return result
    }
}