package pksokolowski.github.com.threegoals.reporter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.report_form.view.*
import pksokolowski.github.com.threegoals.R

class ReportForm@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle) {
    init {
        View.inflate(context, R.layout.report_form, this)
    }

    private val romanNums = arrayOf("Ⅰ", "Ⅱ", "Ⅲ")
}