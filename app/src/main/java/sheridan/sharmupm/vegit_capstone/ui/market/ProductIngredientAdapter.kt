package sheridan.sharmupm.vegit_capstone.ui.market

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

class ProductIngredientAdapter(private var dataSet: List<IngredientName>, private val onClickListener: OnClickListener, private val type: Boolean) :
    RecyclerView.Adapter<ProductIngredientAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtName)
        val addBtn: Button = view.findViewById(R.id.btn_add_ingredient)
        val removeBtn: Button = view.findViewById(R.id.btn_remove_ingredient)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ingredient_product, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (type) {
            viewHolder.addBtn.isVisible = true
            viewHolder.addBtn.setOnClickListener {
                onClickListener.onClick(dataSet[position])
            }
        } else {
            viewHolder.removeBtn.isVisible = true
            viewHolder.removeBtn.setOnClickListener {
                onClickListener.onClick(dataSet[position])
            }
        }

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = dataSet[position].name
    }


    class OnClickListener(val clickListener: (ingredient: IngredientName) -> Unit) {
        fun onClick(ingredient: IngredientName) = clickListener(ingredient)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun setItems(ingredientNames: List<IngredientName>) {
        this.dataSet = ingredientNames
    }

}
