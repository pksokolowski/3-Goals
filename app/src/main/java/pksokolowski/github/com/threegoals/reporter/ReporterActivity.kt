package pksokolowski.github.com.threegoals.reporter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.reporter_activity.*
import pksokolowski.github.com.threegoals.R

class ReporterActivity : AppCompatActivity() {
    companion object {
        private const val EXTRAS_EDITION_ID = "edition_id"
        private const val EXTRAS_EDITION_DAY_NUMBER = "edition_day_number"
        fun newIntent(context: Context, editionID: Long, editionDayNum: Int): Intent {
            return Intent(context, ReporterActivity::class.java).apply {
                putExtra(EXTRAS_EDITION_ID, editionID)
                putExtra(EXTRAS_EDITION_DAY_NUMBER, editionDayNum)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reporter_activity)

        var reportForms = getReportForms()
        for (i in reportForms.indices) {
            reportForms[i].setData(i)
        }
    }

    private fun getReportForms() = arrayOf(
            goal_1 as ReportFormFragment,
            goal_2 as ReportFormFragment,
            goal_3 as ReportFormFragment
    )
}