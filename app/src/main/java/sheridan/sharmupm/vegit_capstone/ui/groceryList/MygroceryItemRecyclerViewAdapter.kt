package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.ui.groceryList.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MygroceryItemRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<MygroceryItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.mCheckBox?.text = item.id


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mDueDateTv: TextView? = null
        var mCheckBox: CheckBox? = null

        fun MyViewHolder(itemView: View) {

            mDueDateTv = itemView.findViewById(R.id.due_date_tv)
            mCheckBox = itemView.findViewById(R.id.mcheckbox)
        }


    }



}