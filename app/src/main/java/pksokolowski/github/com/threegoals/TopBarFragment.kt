package pksokolowski.github.com.threegoals

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.top_bar.*
import kotlinx.android.synthetic.main.top_bar.view.*
import pksokolowski.github.com.threegoals.model.Edition
import javax.inject.Inject


class TopBarFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: MainActivityViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainActivityViewModel::class.java)

        viewModel.getEditions().observe(this, Observer {
            if (it == null) return@Observer
            mView.setupSpinner(it)
        })

    }

    private lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.top_bar, container)
        return mView
    }

    private fun View.setupSpinner(editions: List<Edition>) {
        val spinnerOptions = Array(editions.size) { editions[it].title }

        this.editions_spinner.onItemSelectedListener = null

        val adapter = ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.editions_spinner.adapter = adapter

        this.editions_spinner.setSelection(spinnerOptions.lastIndex)
        // listener
        this.editions_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                viewModel.selectEdition(editions[position])
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
            viewModel.startNewEdition()
            // remove the offer message
            message_holder.removeAllViews()
        }
        message_holder.removeAllViews()
        message_holder.addView(textView)
    }
}