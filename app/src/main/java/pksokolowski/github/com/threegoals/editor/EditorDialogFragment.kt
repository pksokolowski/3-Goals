package pksokolowski.github.com.threegoals.editor

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.reports_editor_dialog.view.*
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.TimeHelper
import pksokolowski.github.com.threegoals.help.HelpProvider
import pksokolowski.github.com.threegoals.model.DaysData
import pksokolowski.github.com.threegoals.model.Edition
import pksokolowski.github.com.threegoals.reporter.ReporterActivity


class EditorDialogFragment : DialogFragment(), DaysDataAdapter.OnItemSelectedListener {
    override fun onItemSelected(dayNum: Int, editionID: Long) {
        context?.startActivity(ReporterActivity.newIntent(requireActivity(), editionID, dayNum))
    }

    companion object {
        private const val FRAGMENT_TAG_LOGS = "reports editor"

        fun showDialog(activity: AppCompatActivity, edition: Edition) {
            val dialog = EditorDialogFragment()
            dialog.mEdition = edition
            dialog.show(activity.supportFragmentManager, FRAGMENT_TAG_LOGS)
        }
    }

    private lateinit var mEdition: Edition
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater.inflate(R.layout.reports_editor_dialog, null)
        mView.help_image.setOnClickListener {
            HelpProvider.requestHelp(requireActivity() as AppCompatActivity, HelpProvider.TOPIC_REPORTS)
        }
        return mView
    }

    override fun onResume() {
        super.onResume()
        if (::mEdition.isInitialized) {
            val data = DaysData(requireActivity(), mEdition)

            mView.editor_recycler.layoutManager = LinearLayoutManager(activity)
            var numOfDaysToShow = data.edition.dayNumOf(TimeHelper.now())
            if (numOfDaysToShow == -1) numOfDaysToShow = data.edition.lengthInDays
            mView.editor_recycler.adapter = DaysDataAdapter(requireActivity(), data, numOfDaysToShow, this)
        }
    }
}