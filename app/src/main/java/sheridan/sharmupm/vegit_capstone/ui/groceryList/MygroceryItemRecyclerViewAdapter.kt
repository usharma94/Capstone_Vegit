package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.each_task.view.*
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery


class MygroceryItemRecyclerViewAdapter: RecyclerView.Adapter<MygroceryItemRecyclerViewAdapter.ViewHolder> {

    var groceryList = mutableListOf<Grocery>()
    private val context: Context

    constructor(context: Context, listGrocery: MutableList<Grocery>) : super() {
        this.context = context
        groceryList.addAll(listGrocery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grocery = groceryList.get(position)
        holder.mCheckBox?.setText(grocery.grocery)
        holder.mDueDateTv?.setText(grocery.due)
        holder.mCheckBox?.setChecked(toBoolean(grocery.status!!))

    }

    private fun toBoolean(status: Int): Boolean {
        return status != 0
    }

    override fun getItemCount(): Int {
        return groceryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mDueDateTv = itemView.due_date_tv
        var mCheckBox = itemView.mcheckbox









    }

}