package pksokolowski.github.com.threegoals.reporter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.tally_counter.view.*
import pksokolowski.github.com.threegoals.R

class TallyCounter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle) {
    val MAX_VALUE = 999

    init {
        View.inflate(context, R.layout.tally_counter, this)

        add_one_button.setOnClickListener {
            value += 1
        }
    }

    var value: Int
        get() {
            val positivesSoFar = try {
                val numberAsString = positives_edittext.text.toString()
                Integer.valueOf(numberAsString)
            } catch (e: Exception) {
                0
            }
            return positivesSoFar
        }
        set(newValue) {
            val validatedValue = Math.min(newValue, MAX_VALUE)
            positives_edittext.setText(validatedValue.toString())
        }
}