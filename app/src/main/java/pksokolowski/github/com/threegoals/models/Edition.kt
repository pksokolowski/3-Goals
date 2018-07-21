package pksokolowski.github.com.threegoals.models

import pksokolowski.github.com.threegoals.TimeHelper

data class Edition (val ID: Long, val title: String, val goals_count: Int, val start_day_timestamp: Long, val length_in_days: Int){
    fun dayNumOf(timestamp: Long): Int{
        if(timestamp < start_day_timestamp) return -1
        if(timestamp > start_day_timestamp + (TimeHelper.dayInMillis * length_in_days)) return -1

        val sinceEditionStart = timestamp - start_day_timestamp
        return (sinceEditionStart / TimeHelper.dayInMillis).toInt()
    }
}