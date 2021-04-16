package sheridan.sharmupm.vegit_capstone.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R


class SearchDialogAdapter(
    private val mDataset: String,
    internal var recyclerViewItemClickListener: SearchFragment
) : RecyclerView.Adapter<SearchDialogAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): IngredientViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)

        return IngredientViewHolder(v)

    }

    override fun onBindViewHolder(ingredientViewHolder: IngredientViewHolder, i: Int) {
        ingredientViewHolder.mTextView.text = mDataset

    }

    override fun getItemCount(): Int {
        return mDataset.length
    }


    inner class IngredientViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        var mTextView: TextView

        init {
            mTextView = v.findViewById(R.id.textView)
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
//            TODO("Not yet implemented")
        }


    }

    interface RecyclerViewItemClickListener {
        fun clickOnItem(data: String)
    }
}