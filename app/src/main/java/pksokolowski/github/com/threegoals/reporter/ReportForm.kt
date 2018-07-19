package pksokolowski.github.com.threegoals.reporter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.likert_scale.*
import kotlinx.android.synthetic.main.report_form.*
import pksokolowski.github.com.threegoals.R

class ReportForm : Fragment() {
    private val romanNums = arrayOf("Ⅰ", " Ⅱ", "Ⅲ", "?")
    private lateinit var mView: View

    companion object {
        private const val ARG_POSITION = "position"
        private const val ARG_NAME = "name"
        private const val ARG_SCORE_POSITIVES = "positives"
        private const val ARG_SCORE_TRYING_HARD = "trying_hard"

        fun newInstance(position: Int, name: String? = null, scorePositives: Int = 0, scoreTryingHard: Int = -1) = ReportForm().apply {
            arguments = Bundle().apply {
                ARG_POSITION to position
                ARG_NAME to name
                ARG_SCORE_POSITIVES to scorePositives
                ARG_SCORE_TRYING_HARD to scoreTryingHard
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.report_form, container)

        if (arguments != null) {
            val args = arguments as Bundle
            with(args) {
                // ensure position is within indices of array of supported positions
                val position = args.getInt(ARG_POSITION, -1)
                val goalNumber = if (position >= 0 && position < romanNums.size) position else romanNums.size - 1

                goal_official_name.text = romanNums[goalNumber]
                goal_custom_name.setText(args.getString(ARG_NAME, ""))
                likert_scale.value = args.getInt(ARG_SCORE_TRYING_HARD, -1)
                tally_counter.value = args.getInt(ARG_SCORE_POSITIVES, 0)
            }
        }

        return mView
    }
}