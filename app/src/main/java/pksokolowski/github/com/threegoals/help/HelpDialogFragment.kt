package pksokolowski.github.com.threegoals.help

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.help_dialog.view.*
import pksokolowski.github.com.threegoals.R

class HelpDialogFragment : DialogFragment() {
    private lateinit var mView: View
    // tab to be used as default on startup
    private var startupTabIndex = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView = inflater.inflate(R.layout.help_dialog, null)

        setupTabs()

        return mView
    }

    fun setInitialTopic(topic: Int) {
        startupTabIndex = topic
    }

    private fun setupTabs() {
        val pagerAdapter = FixedTabsPagerAdapter(childFragmentManager, requireActivity())
        mView.view_pager.adapter = pagerAdapter
        mView.tab_layout.setupWithViewPager(mView.view_pager)

        if (startupTabIndex != -1) {
            mView.view_pager.currentItem = startupTabIndex
        }
    }
}