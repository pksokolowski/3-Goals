package pksokolowski.github.com.threegoals

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import pksokolowski.github.com.threegoals.charts.PieChart
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment
import pksokolowski.github.com.threegoals.models.DaysData
import pksokolowski.github.com.threegoals.models.Edition
import pksokolowski.github.com.threegoals.notifications.NotificationsManager

class MainActivity : AppCompatActivity() {
    private lateinit var data: DaysData
    private lateinit var topBar: TopBarFragment
    private var currentChartSelection = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationsManager.createNotificationChannels(this)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        topBar = (top_bar as TopBarFragment)

        val edition = topBar.getSelectedEdition()
        data = DaysData(this, edition)

        pieChart.noDataMessage = getString(R.string.main_pie_no_data_message)
        pieChart.mainColor = ContextCompat.getColor(this, R.color.colorPrimary)
        pieChart.notSelectedColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

        showData()
        setupListeners(edition)
    }

    private fun showData() {
        displayPieChart()
        displayBarChart()
    }

    private fun displayPieChart() {
            val pieData = mutableListOf<PieChart.Datum>()
            val scoresPerGoal = data.getScoresPerGoal()
            for (i: Int in scoresPerGoal.indices) {
                pieData.add(PieChart.Datum(data.getGoalInitialAt(i),
                        scoresPerGoal[i].toLong(),
                        i.toLong()))
            }

            pieChart.data = pieData
    }

    fun displayBarChart() {
            charts_holder.removeAllViews()
            charts_holder.addView(ChartProvider.getChart(this, data, currentChartSelection, pieChart.lastTouchedIndex))
    }

    private fun setupListeners(edition: Edition?) {
        val selectionButtons = selection_buttons as SelectorButtonsFragment
        selectionButtons.setData(resources.getStringArray(R.array.charts_selection), currentChartSelection)
        selectionButtons.selectionChangedListener = {
                currentChartSelection = it
                displayBarChart()
        }

        if (edition != null) {
            editor_imageview.setOnClickListener {
                    EditorDialogFragment.showDialog(this, data.edition)
            }
        }

        pieChart.sliceSelectionChanged = {
            displayBarChart()
        }

        topBar.editionSelected = {
            data = DaysData(this, it)
            showData()
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
