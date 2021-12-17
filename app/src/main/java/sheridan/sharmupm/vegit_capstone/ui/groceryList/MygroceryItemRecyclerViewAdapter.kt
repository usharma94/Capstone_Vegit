package sheridan.sharmupm.vegit_capstone.ui.groceryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.each_task.view.*
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.controllers.groceryList.GroceryListViewModel
import sheridan.sharmupm.vegit_capstone.models.groceryList.Grocery


class MygroceryItemRecyclerViewAdapter(private val listener:OnItemClickListener):
    RecyclerView.Adapter<MygroceryItemRecyclerViewAdapter.ViewHolder>() {


    var groceryList = mutableListOf<Grocery>()
    private var viewModel: GroceryListViewModel = GroceryListViewModel()

    fun clearList(){
        groceryList.clear()
    }

    fun setList(groceryList: List<Grocery>) {
        this.groceryList = groceryList.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        val grocery = groceryList.get(position)
        viewModel.deleteGrocery(grocery.id!!)
        groceryList.removeAt(position)
        notifyItemRemoved(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val grocery = groceryList.get(position)
        holder.mCheckBox?.text = grocery.name
        holder.mDueDateTv?.text = grocery.due
        holder.mCheckBox?.isChecked = toBoolean(grocery.status!!)
        holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.flipGroceryStatus(grocery.id!!)
            } else {
                viewModel.flipGroceryStatus(grocery.id!!)
            }
        }
    }

    private fun toBoolean(status: Int): Boolean {
        return status != 0
    }

    override fun getItemCount(): Int {
        return groceryList.size
    }

    //set UI for each row
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var mDueDateTv = itemView.due_date_tv
        var mCheckBox = itemView.mcheckbox

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }
}