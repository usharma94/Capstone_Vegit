

package sheridan.sharmupm.vegit_capstone.ui.user

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.products.Product

class ApproveDialogFragment(
    var fragment: Fragment,
    var productDetail: Product,
    context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.approve_dialog)
        val approveDialogProductName = findViewById<TextView>(R.id.approve_diet_detail)
        val approveDialogDietType = findViewById<TextView>(R.id.approve_diet_type)
        val recyclerView: RecyclerView = findViewById(R.id.approveProductIngredients)

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        approveDialogProductName.text = productDetail.name
        approveDialogDietType.text = productDetail.diet_name

        val searchAdapter = IngredientAdapter(productDetail.ingredients)
        recyclerView.adapter = searchAdapter

        findViewById<Button>(R.id.approve_dialog_accept).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        dismiss()
    }
}