package pksokolowski.github.com.threegoals

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.top_bar.*
import kotlinx.android.synthetic.main.top_bar.view.*
import pksokolowski.github.com.threegoals.model.Edition


class TopBarFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var editions: Array<Edition>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.top_bar, container)
        loadEditions()
        mView.setupSpinner()
        return mView
    }

    fun getSelectedEdition(): Edition {
        val current = EditionsManager.getCurrentEdition(requireContext())
        if (current != null) return current

        // no edition right now, it's after the last one for sure
        // because EditionsManager will create one if no previous editions
        // exist, and return it. So since it's after the last edition
        // display previous one. Before returning though, offer the
        // user a way to start a new edition
        makeNewEditionOffer()
        return editions.last()
    }

    private fun loadEditions() {
        editions = EditionsManager.getAllEditions(requireContext())
    }

    private fun View.setupSpinner() {
        val spinnerOptions = Array(editions.size) { editions[it].title }

        this.editions_spinner.onItemSelectedListener = null

        val adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.editions_spinner.adapter = adapter

        this.editions_spinner.setSelection(spinnerOptions.lastIndex)
        // listener
        this.editions_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                editionSelected?.invoke(editions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun makeNewEditionOffer() {
        // if there already is an ongoing edition, return
        val current = EditionsManager.getCurrentEdition(requireContext())
        if (current != null) return

        // create a small UI piece as an interactive message to the user
        val textView = TextView(requireContext())
        textView.text = getString(R.string.top_bar_message_start_new_edition)
        textView.setTypeface(textView.typeface, Typeface.BOLD)
        textView.setOnClickListener {
            // create new edition,
            val newEdition = EditionsManager.createEdition(requireContext())
            // load new data
            loadEditions()
            // display all editions on the spinner's list
            mView.setupSpinner()
            // let listener know about selection change
            editionSelected?.invoke(newEdition)
            // remove the offer message
            message_holder.removeAllViews()
        }
        message_holder.removeAllViews()
        message_holder.addView(textView)
    }

    var editionSelected: ((edition: Edition) -> Unit)? = null
}