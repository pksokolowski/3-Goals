package pksokolowski.github.com.threegoals.help

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import pksokolowski.github.com.threegoals.R

internal class FixedTabsPagerAdapter(fm: FragmentManager, private val mContext: Context) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment? {
        when (position) {
            HelpProvider.TOPIC_GETTING_STARTED -> return UniversalHelpPageFragment.getFragWithContent(mContext.getString(R.string.help_universal_getting_started))
            HelpProvider.TOPIC_CHARTS -> return UniversalHelpPageFragment.getFragWithContent(mContext.getString(R.string.help_universal_charts))
            HelpProvider.TOPIC_REPORTS -> return UniversalHelpPageFragment.getFragWithContent(mContext.getString(R.string.help_universal_reports))
        }
        return null
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            HelpProvider.TOPIC_GETTING_STARTED -> return mContext.getString(R.string.help_topic_getting_started)
            HelpProvider.TOPIC_CHARTS -> return mContext.getString(R.string.help_topic_charts)
            HelpProvider.TOPIC_REPORTS -> return mContext.getString(R.string.help_topic_reports)
        }
        return super.getPageTitle(position)
    }

    override fun isViewFromObject(view: View, potentialOwnerObject: Any): Boolean {
        val potentialOwner = potentialOwnerObject as ViewOwner
        return potentialOwner.owns(view)
    }
}
