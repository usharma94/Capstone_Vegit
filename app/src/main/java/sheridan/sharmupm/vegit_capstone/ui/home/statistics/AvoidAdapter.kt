package sheridan.sharmupm.vegit_capstone.ui.home.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.products.Product

class AvoidAdapter(private val dataSet: List<Product>, private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<AvoidAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txtName)
        val img: ImageView = view.findViewById(R.id.imgProduct)

        init {
            // Define click listener for the ViewHolder's View.
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_item_horizontal, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = dataSet[position].name
        Picasso.get().load(dataSet[position].img_url).placeholder(R.drawable.leaves).error(R.drawable.leaves).into(viewHolder.img)
        viewHolder.itemView.setOnClickListener {
            onClickListener.onClick(dataSet[position])
        }
    }

    class OnClickListener(val clickListener: (product: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}
