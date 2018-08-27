package pksokolowski.github.com.threegoals.reporter

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.reporter_activity.*
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.reporter.ReporterActivityViewModel.Companion.DATA_LOAD_FAILURE
import pksokolowski.github.com.threegoals.utils.ImmersiveAppCompatActivity
import javax.inject.Inject

class ReporterActivity : ImmersiveAppCompatActivity() {
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ReporterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reporter_activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReporterActivityViewModel::class.java)

        obtainDataFromExtras(intent.extras)
        showData()

        done_button.setOnClickListener {
            if (!isInputValid()) return@setOnClickListener
            val forms = getReportForms()

            if (viewModel.getNumOfReportsToModify() == 0) {
                viewModel.saveBrandNewReports(forms)
                finish()
            } else {
                viewModel.editExistingReports(forms)
                finish()
            }
        }

        reporter_constraint_layout.setOnApplyWindowInsetsListener insetsListener@{ _, windowInsets ->
            reporter_constraint_layout.setPadding(0, 0, 0,
                    windowInsets.systemWindowInsetBottom)
            return@insetsListener windowInsets.consumeSystemWindowInsets()
        }
    }

    private fun isInputValid(): Boolean {
        val forms = getReportForms()
        for (f in forms) {
            if (f.getTryingHardScore() == -1) {
                Toast.makeText(this,
                        getString(R.string.reporter_activity_error_select_values),
                        Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    private fun obtainDataFromExtras(bundle: Bundle?) {
        if (bundle == null) return
        val editionID = bundle.getLong(EXTRAS_EDITION_ID, -1)
        val dayNum = bundle.getInt(EXTRAS_EDITION_DAY_NUMBER, -1)

        val result = viewModel.loadData(editionID, dayNum)
        if (result == DATA_LOAD_FAILURE) {
            done_button.isEnabled = false
            throw RuntimeException("failed to load data in ReporterActivity")
        }
    }

    private fun showData() {
        date_textview.text = viewModel.getDate()
        val reportForms = getReportForms()
        viewModel.fillReportFormsWithData(reportForms)
    }


    private fun getReportForms() = arrayOf(
            goal_1 as ReportFormFragment,
            goal_2 as ReportFormFragment,
            goal_3 as ReportFormFragment
    )
}