package pksokolowski.github.com.threegoals

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.constraint.ConstraintSet.*
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.util.TypedValue


class SelectorButtonsFragment : Fragment() {
    private lateinit var lines: MutableList<View>
    private lateinit var buttons: MutableList<Button>
    private lateinit var layout: ConstraintLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = ConstraintLayout(context)
        return generateLayout(arrayOf(" "))
    }

    private fun highlightLink(pos: Int) {
        for (i: Int in lines.indices) {
            if (i != pos) lines[i].setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.selector_inactive_connection))
            else
                lines[i].setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.colorAccent))
        }
    }

    public fun setData(titles: Array<String>, defaultSelection: Int) {
        generateLayout(titles)
        if (defaultSelection in titles.indices) highlightLink(defaultSelection)
    }

    private fun generateLayout(titles: Array<String>): View {
        layout.removeAllViews()
        val set = ConstraintSet()
        val n = titles.size

        buttons = MutableList(n) { Button(context) }
        setListeners()
        lines = MutableList(n) { View(context) }

        // assign IDs first, so the views can be addressed in connect() calls below
        for (i: Int in 0 until n) {
            val line = lines[i]
            val button = buttons[i]
            line.id = View.generateViewId()
            button.id = View.generateViewId()

            // add views to the layout
            layout.addView(line)
            layout.addView(button)

            // set line's dimensions and color
            set.constrainHeight(line.id, dpToPixels(16))
            set.constrainWidth(line.id, dpToPixels(4))
            line.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.selector_inactive_connection))

            // set button's dimensions and text
            set.constrainHeight(button.id, WRAP_CONTENT)
            set.constrainMaxWidth(button.id, WRAP_CONTENT)
            button.text = titles[i]

            set.connect(line.id, TOP, PARENT_ID, TOP, 0)
            set.connect(line.id, START, button.id, START, 0)
            set.connect(line.id, END, button.id, END, 0)

            set.connect(button.id, TOP, PARENT_ID, TOP, dpToPixels(10))


        }

        if (n > 1) {
            val chainViews = IntArray(n) { i -> buttons[i].id }
            val chainWeights = FloatArray(n) { 1f }

            set.createHorizontalChain(PARENT_ID, ConstraintSet.LEFT,
                    PARENT_ID, ConstraintSet.RIGHT,
                    chainViews, chainWeights,
                    ConstraintSet.CHAIN_SPREAD_INSIDE)
        }

        set.applyTo(layout)
        return layout
    }

    private fun setListeners() {
        for (i: Int in buttons.indices) {
            buttons[i].setOnClickListener {
                selectionChangedListener?.invoke(i)
                highlightLink(i)
            }
        }
    }

    private fun dpToPixels(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).toInt()

    var selectionChangedListener: ((i: Int) -> Unit)? = null

}