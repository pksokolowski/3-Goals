package pksokolowski.github.com.threegoals

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import pksokolowski.github.com.threegoals.charts.ChartProvider
import pksokolowski.github.com.threegoals.charts.PieChart
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment
import pksokolowski.github.com.threegoals.help.HelpProvider
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.utils.ColorHelper
import pksokolowski.github.com.threegoals.utils.TimeHelper
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    // todo: remove data from Activity

    private lateinit var topBar: TopBarFragment
    private var currentChartSelection = 1

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        topBar = (top_bar as TopBarFragment)

        pieChart.noDataMessage = getString(R.string.main_pie_no_data_message)
        pieChart.mainColor = ContextCompat.getColor(this, R.color.pieChartPrimary)

        viewModel.getData().observe(this, Observer {
            if (it == null) return@Observer
            setupListeners(it)
            showData(it)
        })
    }

    private fun showData(data: DaysData) {
        displayPieChart(data)
        displayBarChart(data)
    }

    private fun displayPieChart(data: DaysData) {
        val pieData = mutableListOf<PieChart.Datum>()
        val daysOfEditionPassed = data.edition.daysOfEditionPassed(TimeHelper.now())
        val tryingHardPerGoal = data.getScoresPerGoal(includePositives = false, dayCap = daysOfEditionPassed)
        val positivesPerGoal = data.getScoresPerGoal(includeTryingHard = false, dayCap = daysOfEditionPassed)
        val sliceColors = ColorHelper.getScoreInColor(positivesPerGoal.values, positivesPerGoal.maxValue)
        for (i in 0 until data.edition.goalsCount) {
            pieData.add(PieChart.Datum(data.getGoalInitialAt(i), tryingHardPerGoal.values[i].toLong(), i.toLong(), sliceColors[i]))
        }

        pieChart.data = pieData
    }

    private fun displayBarChart(data: DaysData) {
        charts_holder.removeAllViews()
        charts_holder.addView(ChartProvider.getChart(this, data, currentChartSelection, pieChart.lastTouchedIndex))
    }

    private fun setupListeners(data: DaysData) {
        val selectionButtons = selection_buttons as SelectorButtonsFragment
        selectionButtons.setData(resources.getStringArray(R.array.charts_selection), currentChartSelection)
        selectionButtons.selectionChangedListener = {
            currentChartSelection = it
            displayBarChart(data)
        }

        editor_imageview.setOnClickListener {
            EditorDialogFragment.showDialog(this)
        }

        help_imageview.setOnClickListener {
            HelpProvider.requestHelp(this, HelpProvider.TOPIC_GETTING_STARTED)
        }

        pieChart.sliceSelectionChanged = {
            displayBarChart(data)
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
