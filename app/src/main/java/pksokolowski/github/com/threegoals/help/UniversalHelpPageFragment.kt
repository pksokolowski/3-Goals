package pksokolowski.github.com.threegoals.help

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import pksokolowski.github.com.threegoals.R

import java.util.regex.Pattern

class UniversalHelpPageFragment : Fragment(), ViewOwner {

    private var myView: View? = null
    private var myMarkup: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        myView = inflater.inflate(R.layout.help_universal_page_fragment, container, false)
        populateLayoutWithParagraphs()
        return myView
    }

    private fun populateLayoutWithParagraphs() {
        val paragraphsLayout = myView!!.findViewById<LinearLayout>(R.id.paragraphs_layout)
        val ps = myMarkup!!.split(Pattern.quote("[/p]").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (s in ps) {
            val pIndex = s.indexOf("[p]")
            if (pIndex == -1) continue

            val title = s.substring(0, pIndex).trim { it <= ' ' }
            val text = s.substring(pIndex + 3).trim { it <= ' ' }

            val p = Paragraph(requireContext())
            p.setContent(title, text)
            paragraphsLayout.addView(p)
        }
    }

    override fun owns(view: View): Boolean {
        return view === myView
    }

    private class Paragraph : LinearLayout {

        constructor(context: Context) : super(context) {
            setup(context)
        }

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            setup(context)
        }

        private fun setup(context: Context) {
            View.inflate(context, R.layout.help_universal_page_fragment_paragraph, this)
        }

        fun setContent(title: String, text: String) {
            val titleTextView = findViewById<TextView>(R.id.title)
            val textTextView = findViewById<TextView>(R.id.text)

            titleTextView.text = title
            textTextView.text = text
        }
    }

    companion object {

        /**
         * use this method to get the fragment with markup set in it.
         * @param markup markup in the following format:
         * title[p]paragraph text[/p]Another title[p]another paragraph[/p]
         * @return
         */
        fun getFragWithContent(markup: String): UniversalHelpPageFragment {
            val frag = UniversalHelpPageFragment()
            frag.myMarkup = markup
            return frag
        }
    }
}