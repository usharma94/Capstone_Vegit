package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.ingredients.ClassifyIngredient
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.ui.home.statistics.AvoidAdapter


//Ingredient List Dialog
class CustomListViewDialog(var fragment: Fragment, internal var adapter: RecyclerView.Adapter<*>, var similarProducts: List<Product>?, var ingredients: ArrayList<ClassifyIngredient>,
                           context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null

    internal var recyclerView: RecyclerView? = null
    private var recyclerViewProducts: RecyclerView? = null
    private var similarProductTxt: TextView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    var title:TextView? = null
    var close:Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog_layout)

        recyclerView = findViewById(R.id.recycler_view)
        mLayoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = adapter

        recyclerViewProducts = findViewById(R.id.similarProducts)
        similarProductTxt = findViewById(R.id.txtSimilar)
        title = findViewById(R.id.search_dialog_title)
        close = findViewById(R.id.no)

        if (similarProducts != null) {
            similarProductTxt?.visibility = View.VISIBLE
            recyclerViewProducts?.visibility = View.VISIBLE

            val productsAdapter = AvoidAdapter(similarProducts!!, AvoidAdapter.OnClickListener{})
            recyclerViewProducts?.adapter = productsAdapter
        }

        // If one or more ingredients is AVOID, then the overall result will be AVOID
        // If all safe, then overall will be SAFE
        // Otherwise, the result will be CAUTION. (One or more are unspecified, but no AVOID)
        if (ingredients.any{it.color=="#F1948A"}){
            title?.text="AVOID"
            Toast.makeText(context,"AVOID", Toast.LENGTH_LONG).show()
            title?.setBackgroundColor(Color.parseColor("#F1948A"))

        }
        else if (ingredients.all{it.color=="#ABEBC6"}){
            title?.text = "SAFE"
            Toast.makeText(context,"SAFE", Toast.LENGTH_LONG).show()
            title?.setBackgroundColor(Color.parseColor("#ABEBC6"))
        }
        else{
            title?.text="CAUTION"
            Toast.makeText(context,"CAUTION", Toast.LENGTH_LONG).show()
            title?.setBackgroundColor(Color.parseColor("#F9E79F"))
        }

        findViewById<Button>(R.id.no).setOnClickListener(this)
    }




    override fun onClick(v: View) {
        when (v.id) {
//            R.id.yes -> {
//            }
            R.id.no -> {
                dismiss()
            }
            else -> {
            }
        }//Do Something
        dismiss()
    }
}