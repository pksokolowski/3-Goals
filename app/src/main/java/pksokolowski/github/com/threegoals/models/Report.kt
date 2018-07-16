package pksokolowski.github.com.threegoals.models

data class Report(val ID: Long, val day_num: Int, val time_stamp: Long, val score_trying_hard: Int, val score_positives: Int, val goal: Long)