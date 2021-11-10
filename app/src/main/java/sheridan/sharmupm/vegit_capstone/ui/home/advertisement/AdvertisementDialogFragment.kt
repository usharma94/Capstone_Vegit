

package sheridan.sharmupm.vegit_capstone.ui.home.advertisement

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.products.Product

class AdvertisementDialogFragment(
    var fragment: Fragment,
    var productDetail: Product,
    context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.product_dialog)
        val advertisementDialogProductName = findViewById<TextView>(R.id.advertisement_diet_detail)
        val advertisementDialogDietType = findViewById<TextView>(R.id.advertisement_diet_type)
        val advertisementCategory = findViewById<TextView>(R.id.advertisement_category)
        val advertisementImg = findViewById<ImageView>(R.id.advertisement_img)
        val recyclerView: RecyclerView = findViewById(R.id.advertisementProductIngredients)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        advertisementDialogProductName.text = productDetail.name
        advertisementDialogDietType.text = productDetail.diet_name
        advertisementCategory.text = productDetail.category
        Picasso.get().load(productDetail.img_url).placeholder(R.drawable.leaves).error(R.drawable.leaves).into(advertisementImg)


        val ingredientsAdapter = AdvertisementIngredientAdapter(productDetail.ingredients)
        recyclerView.adapter = ingredientsAdapter

        findViewById<Button>(R.id.search_dialog_done).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}