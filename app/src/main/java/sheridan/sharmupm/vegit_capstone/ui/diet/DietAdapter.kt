package sheridan.sharmupm.vegit_capstone.ui.diet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.DietModel


class DietAdapter(private val dietList: List<DietModel>, val onDietClickListener: (DietModel) -> Unit) :
    RecyclerView.Adapter<DietAdapter.DietViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietAdapter.DietViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DietViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DietViewHolder, position: Int) {
        val diet: DietModel = dietList[position]
        holder.bind(diet)

        holder.itemView.setOnClickListener {
            onDietClickListener(diet)

            notifyDataSetChanged()
        }

        if (!diet.isDisabled) {
            if (diet.isSelected) {
                holder.itemView.setBackgroundColor(Color.argb(100, 0, 255, 0))
            } else {
                holder.itemView.setBackgroundColor(Color.argb(45, 0, 255, 0))
            }
        }
    }

    override fun getItemCount() = dietList.size

    inner class DietViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.item_diet, parent, false)) {
        private var dietImg: ImageView ?= null
        private var dietName: TextView ?= null
        private var dietDescription: TextView ?= null

        init {
            dietImg = itemView.findViewById(R.id.imgDiet)
            dietName = itemView.findViewById(R.id.txtDiet)
            dietDescription = itemView.findViewById(R.id.txtDescription)
        }

        fun bind(diet: DietModel) {
            dietImg?.setImageResource(diet.dietImage!!)
            dietName?.text = diet.dietName
            dietDescription?.text = diet.dietDescription

            if (diet.isSelected)
                itemView.setBackgroundColor(Color.argb(45, 0, 255, 0))
            if (diet.isDisabled)
                itemView.setBackgroundResource(R.color.colorGrey)
        }
    }
}

