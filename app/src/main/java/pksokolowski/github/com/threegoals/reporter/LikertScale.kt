package pksokolowski.github.com.threegoals.reporter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.likert_scale.view.*
import pksokolowski.github.com.threegoals.R

class LikertScale @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ConstraintLayout(context, attrs, defStyle), CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val newValue = likert_radio_group.indexOfChild(buttonView)

        if (isChecked) {
            updateTextualExplaination(newValue)
            value = newValue
        }
    }

    companion object {
        private const val MAX_VALUE = 4 // and min value is 0
    }

    private var mValue = -1
    var value: Int
        get() = mValue
        set(newValue) {
            if (newValue < 0 || newValue > MAX_VALUE) return
            mValue = newValue
            checkRadioButtonForCurrentValue()
        }

    init {
        View.inflate(context, R.layout.likert_scale, this)
        setup()
    }

    private fun setup() {
        var i = 0
        val size = likert_radio_group.childCount
        while (i < size) {
            (likert_radio_group.getChildAt(i) as RadioButton).setOnCheckedChangeListener(this)
            i++
        }
    }

    private fun updateTextualExplaination(numChosen: Int) {
        // mam już numChosen in range 0-4
        // więc daję text.
        // get the text for options
        val textualArray = resources.getStringArray(R.array.likert_textual_explanations)
        val textualExplanation = findViewById<View>(R.id.scale_text_representation_of_current_choice) as TextView
        textualExplanation.text = textualArray[numChosen]
    }

    private fun checkRadioButtonForCurrentValue() {
        if (value < 0 || value > MAX_VALUE) return
        val r = likert_radio_group.getChildAt(value) as RadioButton
        r.isChecked = true
    }

    public override fun onSaveInstanceState(): Parcelable? {
        //begin boilerplate code that allows parent classes to save state
        val superState = super.onSaveInstanceState()

        val ss = SavedState(superState)
        //end

        ss.stateToSave = value

        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        value = state.stateToSave
    }

    internal class SavedState : View.BaseSavedState {
        var stateToSave: Int = 0

        constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            this.stateToSave = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(this.stateToSave)
        }

        companion object {

            //required field that makes Parcelables from a Parcel
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}