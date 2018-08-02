package pksokolowski.github.com.threegoals

import pksokolowski.github.com.threegoals.models.Day
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.models.Report

class ScoreCalculator {
    companion object {

        private const val MAX_POSITIVES_AMOUNT = 999
        private const val MAX_TRYING_HARD_SCORE = 4

        fun calc(report: Report, includeTryingHard: Boolean = true, includePositives: Boolean = true): Int {
            var sum = 0
            if (includeTryingHard) sum += subCalcTryingHard(report.score_trying_hard)
            if (includePositives) sum += subCalcPositives(report.score_positives)
            return sum
        }

        fun calc(day: Day, includeTryingHard: Boolean = true, includePositives: Boolean = true, goalPosition: Int = -1): Int {
            if (goalPosition != -1) {
                return calc(day.reports[goalPosition], includeTryingHard, includePositives)
            }
            var sum = 0
            for (report: Report in day.reports) {
                sum += calc(report, includeTryingHard, includePositives)
            }
            return sum
        }

        private fun subCalcPositives(positives: Int): Int {
            val steepness = 0.04
            val asymptoticMultiplier = 1 - Math.pow(Math.E, steepness * -positives.toDouble())
            val maxValue = 40
            return (maxValue * asymptoticMultiplier).toInt()
        }

        private fun subCalcTryingHard(trying: Int): Int {
            return (Math.pow(trying.toDouble(), 1.2) * 10).toInt()
        }

        fun getMaxDailyScore(edition: Edition): Int {
            return edition.goals_count * getMaxScoreOfReport(edition)
        }

        fun getMaxDailyScore(edition: Edition, includeTryingHard: Boolean = true, includePositives: Boolean = true, forASingleGoal: Boolean = false): Int {
            val report = Report(-1, 0, 0, MAX_TRYING_HARD_SCORE, MAX_POSITIVES_AMOUNT, 0)
            val reportScore = calc(report, includeTryingHard, includePositives)
            return if (forASingleGoal) {
                reportScore
            } else {
                edition.goals_count * reportScore
            }
        }

        fun getMaxScoreOfReport(edition: Edition): Int {
            val report = Report(-1, 0, 0, MAX_TRYING_HARD_SCORE, MAX_POSITIVES_AMOUNT, 0)
            return calc(report)
        }

        fun getMaxPositivesScorePerReport(): Int {
            return subCalcPositives(MAX_POSITIVES_AMOUNT)
        }

        fun getMaxTryingHardScorePerReport(): Int {
            return subCalcTryingHard(MAX_TRYING_HARD_SCORE)
        }
    }
}