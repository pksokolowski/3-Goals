package pksokolowski.github.com.threegoals

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import pksokolowski.github.com.threegoals.charts.PieChart
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment
import pksokolowski.github.com.threegoals.models.DaysData
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.notifications.NotificationsManager

class MainActivity : AppCompatActivity() {
    var data: DaysData? = null
    private var currentChartSelection = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationsManager.createNotificationChannels(this)
        setContentView(R.layout.activity_main)
        val edition = EditionsManager.getCurrentEdition(this)
        if (edition != null) data = DaysData(this, edition)

        pieChart.noDataMessage = getString(R.string.main_pie_no_data_message)
        pieChart.mainColor = ContextCompat.getColor(this, R.color.colorPrimary)
        pieChart.notSelectedColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        if (data != null) {
            val pieData = mutableListOf<PieChart.Datum>()
            val scoresPerGoal = data!!.getScoresPerGoal()
            for (i: Int in scoresPerGoal.indices) {
                pieData.add(PieChart.Datum(data!!.getGoalInitialAt(i),
                        scoresPerGoal[i].toLong(),
                        i.toLong()))
            }

            pieChart.data = pieData
        }

        displayChart()

        setupListeners(edition)
    }

    private fun setupListeners(edition: Edition?) {
        val selectionButtons = selection_buttons as SelectorButtonsFragment
        selectionButtons.setData(resources.getStringArray(R.array.charts_selection), currentChartSelection)
        selectionButtons.selectionChangedListener = {
            if (data != null) {
                currentChartSelection = it
                displayChart()
            }
        }


        if (edition != null) {
            editor_imageview.setOnClickListener {
                val data = data
                if (data != null) {
                    EditorDialogFragment.showDialog(this, data.edition)
                }
            }
        }

        pieChart.sliceSelectionChanged = {
            displayChart()
        }
    }

    fun displayChart() {
        val data = data
        if (data != null) {
            charts_holder.removeAllViews()
            charts_holder.addView(ChartProvider.getChart(this, data, currentChartSelection, pieChart.lastTouchedIndex))
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
