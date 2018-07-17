package pksokolowski.github.com.threegoals.models

import android.content.Context
import pksokolowski.github.com.threegoals.database.DbHelper

class ReportsData(context: Context, val edition: Edition) {
    private var goals = mutableListOf<Goal>()
    private var reports = arrayOfNulls<Report?>(edition.length_in_days)

    init {
        val db = DbHelper.getInstance(context)
        goals = db.getGoals(edition)
        val editionsReports = db.getReports(edition)

        // assign reports to day slots:
        for (i in editionsReports.indices){
            val report = editionsReports[i]
            val day_num = report.day_num
            reports[day_num] = report
        }
    }


}