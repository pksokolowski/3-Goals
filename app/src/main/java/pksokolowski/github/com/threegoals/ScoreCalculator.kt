package pksokolowski.github.com.threegoals

import pksokolowski.github.com.threegoals.models.Day
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.models.Report

class ScoreCalculator {
    companion object {
        fun calc(report: Report): Int{
            return subCalcTryingHard(report.score_trying_hard) + subCalcPositives(report.score_positives)
        }

        fun calc(day: Day): Int{
            var sum = 0
            for(report: Report in day.reports){
                sum += calc(report)
            }
            return sum
        }

        fun subCalcPositives(positives: Int): Int{
            return Math.sqrt(positives.toDouble()).toInt()
        }

        fun subCalcTryingHard(trying: Int): Int{
            return Math.pow(trying.toDouble(), 2.5).toInt()
        }

        fun getMaxDailyScore(edition: Edition): Int{
            val report = Report(-1, 0, 0,4, 999,0)
            return edition.goals_count * calc(report)
        }
    }
}