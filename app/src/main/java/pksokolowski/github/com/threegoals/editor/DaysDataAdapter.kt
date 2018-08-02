package pksokolowski.github.com.threegoals.editor

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.reports_editor_list_item.view.*
import pksokolowski.github.com.threegoals.R
import pksokolowski.github.com.threegoals.models.DaysData
import pksokolowski.github.com.threegoals.reporter.ReporterActivity

class DaysDataAdapter(val context: Context, val data: DaysData, private val numOfDaysToShow: Int, itemSelectedListener: OnItemSelectedListener) : RecyclerView.Adapter<DaysDataAdapter.ItemViewHolder>() {
    private var mListener: OnItemSelectedListener? = null

    init {
        mListener = itemSelectedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.reports_editor_list_item, parent, false))

    override fun getItemCount(): Int {
        return numOfDaysToShow
    }

    override fun onBindViewHolder(holder: ItemViewHolder, rawPosition: Int) {
        val reversedPosition = reverseOrder(rawPosition)

        holder.tvDate.text = data.edition.dateByDayNum(reversedPosition)
        holder.tvStatus.text = if (data.isReportPresentForDay(reversedPosition))
            context.getString(R.string.editor_report_status_submitted) else
            context.getString(R.string.editor_report_status_missing)
        holder.layout.setOnClickListener {
            mListener?.onItemSelected(reversedPosition, data.edition.ID)
        }
    }

    private fun reverseOrder(pos: Int) = (numOfDaysToShow - 1) - pos

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.date
        val tvStatus: TextView = itemView.status
        val layout: ConstraintLayout = itemView.layout
    }

    interface OnItemSelectedListener {
        fun onItemSelected(dayNum: Int, editionID: Long)
    }
}