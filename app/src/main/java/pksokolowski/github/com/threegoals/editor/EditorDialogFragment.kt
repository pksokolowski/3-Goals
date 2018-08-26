package pksokolowski.github.com.threegoals.editor

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.reports_editor_dialog.view.*
import pksokolowski.github.com.threegoals.MainActivityViewModel
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.utils.TimeHelper
import pksokolowski.github.com.threegoals.help.HelpProvider
import pksokolowski.github.com.threegoals.reporter.ReporterActivity
import javax.inject.Inject


class EditorDialogFragment : DialogFragment(), DaysDataAdapter.OnItemSelectedListener {
    override fun onItemSelected(dayNum: Int, editionID: Long) {
        context?.startActivity(ReporterActivity.newIntent(requireActivity(), editionID, dayNum))
    }

    companion object {
        private const val FRAGMENT_TAG_LOGS = "reports editor"

        fun showDialog(activity: AppCompatActivity) {
            val dialog = EditorDialogFragment()
            dialog.show(activity.supportFragmentManager, FRAGMENT_TAG_LOGS)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MainActivityViewModel

    private lateinit var mView: View

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainActivityViewModel::class.java)

        viewModel.getData().observe(this, Observer {
            if (it == null) return@Observer
            mView.editor_recycler.layoutManager = LinearLayoutManager(activity)
            var numOfDaysToShow = it.edition.dayNumOf(TimeHelper.now())
            if (numOfDaysToShow == -1) numOfDaysToShow = it.edition.lengthInDays
            mView.editor_recycler.adapter = DaysDataAdapter(requireActivity(), it, numOfDaysToShow, this)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater.inflate(R.layout.reports_editor_dialog, null)
        mView.help_image.setOnClickListener {
            HelpProvider.requestHelp(requireActivity() as AppCompatActivity, HelpProvider.TOPIC_REPORTS)
        }

        return mView
    }
}