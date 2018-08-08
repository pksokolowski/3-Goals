package pksokolowski.github.com.threegoals

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import pksokolowski.github.com.threegoals.charts.PieChart
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment
import pksokolowski.github.com.threegoals.help.HelpProvider
import pksokolowski.github.com.threegoals.models.DaysData
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

        pieChart.noDataMessage = getString(R.string.main_pie_no_data_message)
        pieChart.mainColor = ContextCompat.getColor(this, R.color.pieChartPrimary)
    }

    override fun onResume() {
        super.onResume()

        val edition = topBar.getSelectedEdition()
        data = DaysData(this, edition)

        showData()
        setupListeners()
    }

    private fun showData() {
        displayPieChart()
        displayBarChart()
    }

    private fun displayPieChart() {
        val pieData = mutableListOf<PieChart.Datum>()
        val daysOfEditionPassed = data.edition.daysOfEditionPassed(TimeHelper.now())
        val tryingHardPerGoal = data.getScoresPerGoal(includePositives = false, dayCap = daysOfEditionPassed)
        val positivesPerGoal = data.getScoresPerGoal(includeTryingHard = false, dayCap = daysOfEditionPassed)
        val sliceColors = ColorHelper.getScoreInColor(positivesPerGoal.values, positivesPerGoal.maxValue)
        for(i in 0 until data.edition.goals_count)
        {
            pieData.add(PieChart.Datum(data.getGoalInitialAt(i), tryingHardPerGoal.values[i].toLong(), i.toLong(), sliceColors[i]))
        }

        pieChart.data = pieData
    }

    private fun displayBarChart() {
        charts_holder.removeAllViews()
        charts_holder.addView(ChartProvider.getChart(this, data, currentChartSelection, pieChart.lastTouchedIndex))
    }

    private fun setupListeners() {
        val selectionButtons = selection_buttons as SelectorButtonsFragment
        selectionButtons.setData(resources.getStringArray(R.array.charts_selection), currentChartSelection)
        selectionButtons.selectionChangedListener = {
            currentChartSelection = it
            displayBarChart()
        }

        editor_imageview.setOnClickListener {
            EditorDialogFragment.showDialog(this, data.edition)
        }

        help_imageview.setOnClickListener {
            HelpProvider.requestHelp(this, HelpProvider.TOPIC_GETTING_STARTED)
        }

        pieChart.sliceSelectionChanged = {
            displayBarChart()
        }

        topBar.editionSelected = lambda@{
            if (data.edition == it) return@lambda
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
