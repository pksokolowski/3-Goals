package pksokolowski.github.com.threegoals.data

import android.provider.BaseColumns

internal object Contract {

    internal class reports : BaseColumns {
        companion object {
            val ID = "_id"
            val TABLE_NAME = "reports"
            val COLUMN_NAME_DAY_NUM = "dayNum"
            val COLUMN_NAME_TIME_STAMP = "timeStamp"
            val COLUMN_NAME_SCORE_TRYING_HARD = "scoreTryingHard"
            val COLUMN_NAME_SCORE_POSITIVES = "scorePositives"
            val COLUMN_NAME_GOAL = "goal"
        }
    }

    internal class goals : BaseColumns {
        companion object {
            val ID = "_id"
            val TABLE_NAME = "goals"
            val COLUMN_NAME_NAME = "name"
            val COLUMN_NAME_INITIAL = "initial"
            val COLUMN_NAME_POSITION = "position"
            val COLUMN_NAME_EDITION = "edition"
        }
    }

    internal class editions : BaseColumns {
        companion object {
            val ID = "_id"
            val TABLE_NAME = "editions"
            val COLUMN_NAME_TITLE = "title"
            val COLUMN_NAME_GOALS_COUNT = "goalsCount"
            val COLUMN_NAME_START_DAY_0_HOUR = "start_day_0_hour"
            val COLUMN_NAME_EDITION_LENGTH = "lengthInDays"
        }
    }
}