package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.ingredients.ClassifyIngredient

class DataAdapter(
        private val mDataset: ArrayList<ClassifyIngredient>,
        internal var recyclerViewItemClickListener: ClassifyproductsFragment
) : RecyclerView.Adapter<DataAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): IngredientViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(v)
    }

    override fun onBindViewHolder(ingredientViewHolder: IngredientViewHolder, i: Int) {
        ingredientViewHolder.mTextView.text = mDataset[i].name
        ingredientViewHolder.mDietView.text = mDataset[i].diet_name
        ingredientViewHolder.itemView.setBackgroundColor(Color.parseColor(mDataset[i].color.toString()))
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    inner class IngredientViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        var mTextView: TextView
        var mDietView: TextView

        init {
            mTextView = v.findViewById(R.id.txtName)
            mDietView = v.findViewById(R.id.txtClassification)
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            recyclerViewItemClickListener.clickOnItem(mDataset[this.adapterPosition].toString())
        }
    }

    interface RecyclerViewItemClickListener {
        fun clickOnItem(data: String)
    }
}