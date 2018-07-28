package pksokolowski.github.com.threegoals.help

import android.support.v7.app.AppCompatActivity


/**
 * Displays help UI for the user. A form of a manual.
 * Contains different "pages", tabs...
 */
object HelpProvider {

    const val TOPIC_GETTING_STARTED = 0
    const val TOPIC_CHARTS = 1
    const val TOPIC_REPORTS = 2

    fun requestHelp(context: AppCompatActivity, topic: Int) {
        val helpDialog = HelpDialogFragment()
        helpDialog.setInitialTopic(topic)
        helpDialog.show(context.supportFragmentManager, "help")
    }
}
