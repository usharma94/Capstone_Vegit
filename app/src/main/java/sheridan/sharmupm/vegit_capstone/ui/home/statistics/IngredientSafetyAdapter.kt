package sheridan.sharmupm.vegit_capstone.ui.home.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

class IngredientSafetyAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientSafetyAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rank: TextView = view.findViewById(R.id.txtRank)
        val name: TextView = view.findViewById(R.id.txtName)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.ingredient_item_stats, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.rank.text = "#" + (position + 1)
        viewHolder.name.text = dataSet[position].name
    }


    class OnClickListener(val clickListener: (ingredient: IngredientName) -> Unit) {
        fun onClick(ingredient: IngredientName) = clickListener(ingredient)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
