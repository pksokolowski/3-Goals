package pksokolowski.github.com.threegoals.reporter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pksokolowski.github.com.threegoals.R

class ReportForm : Fragment() {
    private val romanNums = arrayOf("Ⅰ", "Ⅱ", "Ⅲ")
    private lateinit var mView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.report_form, container)
        return mView
    }
}