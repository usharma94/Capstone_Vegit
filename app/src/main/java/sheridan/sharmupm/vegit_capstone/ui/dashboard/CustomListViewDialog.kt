package sheridan.sharmupm.vegit_capstone.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sheridan.sharmupm.vegit_capstone.R
import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.ui.home.statistics.AvoidAdapter

class CustomListViewDialog(var fragment: Fragment, internal var adapter: RecyclerView.Adapter<*>, var similarProducts: List<Product>?,
                           context: Context
) : Dialog(context),
    View.OnClickListener {
    var dialog: Dialog? = null

    internal var recyclerView: RecyclerView? = null
    private var recyclerViewProducts: RecyclerView? = null
    private var similarProductTxt: TextView? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null


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

        if (similarProducts != null) {
            similarProductTxt?.visibility = View.VISIBLE
            recyclerViewProducts?.visibility = View.VISIBLE

            val productsAdapter = AvoidAdapter(similarProducts!!, AvoidAdapter.OnClickListener{})
            recyclerViewProducts?.adapter = productsAdapter
        }

//        findViewById<Button>(R.id.yes).setOnClickListener(this)
        findViewById<Button>(R.id.no).setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
//            R.id.yes -> {
//            }
            R.id.no -> dismiss()
            else -> {
            }
        }//Do Something
        dismiss()
    }
}