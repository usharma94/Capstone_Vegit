package sheridan.sharmupm.vegit_capstone.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

class SearchAdapter(private val dataSet: List<IngredientName>, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val searchRes: TextView = view.findViewById(R.id.txt_search_results)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_search, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.searchRes.text = dataSet[position].name
        viewHolder.searchRes.setOnClickListener {
            onClickListener.onClick(dataSet[position])
        }

    }


    class OnClickListener(val clickListener: (ingredient: IngredientName) -> Unit) {
        fun onClick(ingredient: IngredientName) = clickListener(ingredient)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
