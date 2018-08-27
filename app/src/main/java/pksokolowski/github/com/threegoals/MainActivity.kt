package pksokolowski.github.com.threegoals

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import pksokolowski.github.com.threegoals.charts.ChartProvider
import pksokolowski.github.com.threegoals.charts.PieChartDataConverter
import pksokolowski.github.com.threegoals.editor.EditorDialogFragment
import pksokolowski.github.com.threegoals.help.HelpProvider
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.utils.ImmersiveAppCompatActivity
import javax.inject.Inject

class MainActivity : ImmersiveAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

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
        pieChart.data = PieChartDataConverter.convert(data)
    }

    private fun displayBarChart(data: DaysData) {
        charts_holder.removeAllViews()
        charts_holder.addView(ChartProvider.getChart(this, data, viewModel.currentChartSelection, pieChart.lastTouchedIndex))
    }

    private fun setupListeners(data: DaysData) {
        val selectionButtons = selection_buttons as SelectorButtonsFragment
        selectionButtons.setData(resources.getStringArray(R.array.charts_selection), viewModel.currentChartSelection)
        selectionButtons.selectionChangedListener = {
            viewModel.currentChartSelection = it
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
}
