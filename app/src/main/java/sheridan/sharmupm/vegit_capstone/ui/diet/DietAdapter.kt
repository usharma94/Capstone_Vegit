package sheridan.sharmupm.vegit_capstone.ui.diet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.DietModel


class DietAdapter(
        private val dietList: List<DietModel>,
        private val onClickListener: OnClickListener
) :
    ListAdapter<DietModel, DietAdapter.DietViewHolder>(
            DiffCallback
    ) {

    class DietViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDiet: ImageView = itemView.findViewById(R.id.imgDiet)
        val dietName: TextView = itemView.findViewById(R.id.txtDiet)
        val dietDescription: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietViewHolder {
        return DietViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_diet,
                        parent,
                        false
                )
        )
    }


    var selectedItems = mutableListOf<Int>(-1)

    // ******this should be refactored to be better organized********
    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val item: DietModel = dietList[position]
        holder.dietName.text = item.dietName
        holder.dietDescription.text = item.dietDescription
        item.dietImage?.let { holder.imgDiet.setBackgroundResource(it) }
        holder.itemView.setBackgroundColor(Color.WHITE)

        selectedItems.forEach {
            if (it == position) {
                if (it != 2 ){
                    holder.itemView.setBackgroundColor(Color.argb(100, 0, 255, 0))
                    //println("UPMA SHARMA")
                    //print(position)
                }
//                holder.itemView.visibility = INVISIBLE
            }
            else{
//                holder.itemView.visibility = VISIBLE
                if(position == 2){
                    holder.itemView.setBackgroundResource(R.color.colorGrey)
                }else{
                    holder.itemView.setBackgroundColor(Color.argb(45, 0, 255, 0))
                }

            }
        }

        holder.itemView.setOnClickListener {
            selectedItems.add(position)
            selectedItems.forEach { selectedItem ->  // this forEach is required to refresh all the list
                notifyItemChanged(selectedItem)
            }

        }
    }

    override fun getItemCount() = dietList.size

    class OnClickListener(val clickListener: (diet: DietModel) -> Unit) {
        fun onClick(diet: DietModel) = clickListener(diet)

    }

    companion object DiffCallback : DiffUtil.ItemCallback<DietModel>() {
        override fun areItemsTheSame(oldItem: DietModel, newItem: DietModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DietModel, newItem: DietModel): Boolean {
            return oldItem.dietName == newItem.dietName
        }
    }
}

